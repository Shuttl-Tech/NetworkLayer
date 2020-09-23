package app.goplus.lib.custom

import app.goplus.lib.R
import app.goplus.lib.models.*
import app.goplus.lib.network.Network
import app.goplus.lib.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ResultCall<T>(proxy: Call<T>, private val retryPolicy: RetryPolicy?) :
    CallDelegate<T, ApiResult<*>>(proxy) {

    override fun cloneImpl() = ResultCall(proxy.clone(), retryPolicy = retryPolicy)

    override fun enqueueImpl(callback: Callback<ApiResult<*>>) =
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                var result: ApiResult<*>? = null
                if (code in HttpURLConnection.HTTP_OK until HttpURLConnection.HTTP_MULT_CHOICE) {
                    val body = response.body()
                    result = if (body is ApiResult<*>) {
                        // creating new result for the responses where data is sent but isSuccess is false
                        ApiResult(
                            data = body.data,
                            title = body.title,
                            cached = body.cached,
                            message = body.message,
                            throwable = null,
                            responseCode = code
                        )
                    } else {
                        ApiResult.success(body, responseCode = code)
                    }
                } else {
                    Network.reValidateUser(code)
                    var errorModel: ErrorModel? = null
                    val body = response.body()
                    if (body is ApiResult<*>) {
                        errorModel = body.error
                    }
                    result = ApiResult.error(
                        data = null,
                        message = response.message(),
                        responseCode = code,
                        error = errorModel
                    )
                }

                if (result.success == true || retryPolicy?.retry(proxy, this) != true) {
                    callback.onResponse(this@ResultCall, Response.success(result))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (retryPolicy?.retry(proxy, this) != true) {
                    var errorModel: ErrorModel? = null
                    if (!NetworkUtils.isInternetConnected(Network.context)) {
                        errorModel = ErrorModel(
                            ErrorType.INTERNET,
                            Network.context?.getString(R.string.gps_error_description),
                            ErrorParams(
                                Network.context?.getString(R.string.gps_error_heading),
                                Network.context?.getString(R.string.gps_error_cta),
                                ErrorActions.ENABLE_NETWORK,
                                DisplayType.FULL_SCREEN
                            )
                        )
                    }
                    val result =
                        ApiResult.error(data = null, message = t.message ?: "", throwable = t, error = errorModel)
                    callback.onResponse(this@ResultCall, Response.success(result))
                }
            }
        })
}
