package app.goplus.lib.custom

import app.goplus.lib.models.ApiResult
import app.goplus.lib.network.Network
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
                    result = ApiResult.error(
                        data = null,
                        message = response.message(),
                        responseCode = code
                    )
                }

                if (result.success == true || retryPolicy?.retry(proxy, this) != true) {
                    callback.onResponse(this@ResultCall, Response.success(result))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (retryPolicy?.retry(proxy, this) != true) {
                    val result =
                        ApiResult.error(data = null, message = t.message ?: "", throwable = t)
                    callback.onResponse(this@ResultCall, Response.success(result))
                }
            }
        })
}
