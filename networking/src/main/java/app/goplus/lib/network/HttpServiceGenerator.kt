package app.goplus.lib.network

import android.content.Context
import android.os.Build
import app.goplus.lib.BuildConfig
import app.goplus.lib.custom.ResultCallAdapterFactory
import app.goplus.lib.helpers.NoConnectivityException
import app.goplus.lib.network.KeyType.Companion.googleReactive
import app.goplus.lib.network.KeyType.Companion.normal
import app.goplus.lib.network.KeyType.Companion.reactive
import app.goplus.lib.network.KeyType.Companion.simple
import app.goplus.lib.utils.NetworkUtils
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

private const val TIMEOUT_RESPONSE: Long = 30
private const val TIMEOUT_CONNECTION: Long = 10
private val retrofitMap = HashMap<String, Retrofit?>()
//URL's to be used
private val logging = HttpLoggingInterceptor()

/**
 * Generates the instance for provided service class
 *
 * @param context         in which context service will be fired
 * @param serviceClass    which service is required
 * @param responseTimeout if any custom timeout is required
 * @return
 */
@JvmOverloads
fun <S> generate(
    context: Context,
    type: String,
    serviceClass: Class<S>,
    responseTimeout: Long = TIMEOUT_RESPONSE
): S {
    val key = getRetrofitKey(type, context)
    val retrofit: Retrofit = retrofitMap[key]
            ?: createRetrofit(context, type, responseTimeout).also { retrofitMap[key] = it }
    return retrofit.create(serviceClass)
}

fun createRetrofit(context: Context, type: String, responseTimeout: Long): Retrofit {
    var builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl(getIntendedUrl(context, type))
        .client(getHttpClient(type, context, responseTimeout))

    if (type == reactive || type == googleReactive) {
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }
    if (type == simple) {
        builder.addCallAdapterFactory(ResultCallAdapterFactory.create())
    }
    return builder.build()
}

private fun getRetrofitKey(type: String, context: Context): String {
    return getIntendedUrl(context, type) + type
}

@JvmOverloads
fun getIntendedUrl(context: Context, type: String = normal): String {
    return Network.getBaseUrl(type)
}

private fun getHttpClient(objType: String, context: Context, responseTimeout: Long): OkHttpClient {

    var client = OkHttpClient.Builder()
        .readTimeout(responseTimeout, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)


    if (BuildConfig.BUILD_TYPE.equals(ENV_RELEASE, ignoreCase = true)) {
        // For PROD environments
        logging.level = HttpLoggingInterceptor.Level.NONE
    } else {

        // For basic information logging
        //logging.setLevel(Level.BASIC);

        // For basic + headers information logging
        //logging.setLevel(Level.HEADERS);

        // For detailed information logging
        // [IMPORTANT] Use this level only if necessary
        // because logs will clutter our Android monitor if we’re receiving large data sets
        logging.level = HttpLoggingInterceptor.Level.BODY
    }

    client.interceptors().addAll(getInterceptor(objType, context))

    return client.build()
}

private fun getInterceptor(objType: String, context: Context): List<Interceptor> {
    var intercepters = mutableListOf<Interceptor>()
    when (objType) {
        googleReactive -> intercepters.add(GoogleRequestHeaderInterceptor(context))
        else -> {
            intercepters.add(RequestHeaderInterceptor())
            intercepters.add(ConnectivityInterceptor(context))
        }
    }
    intercepters.add(logging)
    for (interceptor in Network.getExtraInterceptors()) {
        intercepters.add(interceptor)
    }
    return intercepters
}

/**
 * Helper class which performs the default tasks like adding query params to the [Interceptor]
 * for the GoogleDirectionsApi.
 */
private class GoogleRequestHeaderInterceptor internal constructor(private val context: Context) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val url =
            request.url().newBuilder().addQueryParameter("key", Network.getGoogleKey()).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}


/**
 * Interceptor to add default headers to request
 */
private class RequestHeaderInterceptor internal constructor() : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        var builder = original.newBuilder()
            .header("Accept", "application/json")
            .addHeader("Accept-Language", Locale.getDefault().toString())
            .addHeader("timezone", TimeZone.getDefault().id)
            .addHeader("platform", "Android")
            .addHeader("deviceModel", Build.MODEL)
            .addHeader("deviceManufacturer", Build.MANUFACTURER)
            .addHeader("deviceVersion", Build.VERSION.SDK_INT.toString())

        Network.getHeaders()?.let {
            for (pair in it) {
                builder.addHeader(pair.key, pair.value)
            }
        }

        return chain.proceed(builder.build())
    }
}

/**
 * Helper class adds an interceptor to check for internet connectivity.
 * In case of no connectivity a [NoConnectivityException] is thrown and the
 * request chain is not proceeded.
 */
private class ConnectivityInterceptor internal constructor(private val mContext: Context) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtils.isInternetConnected(mContext)) {
            throw NoConnectivityException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}