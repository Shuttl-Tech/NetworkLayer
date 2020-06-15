package app.goplus.lib.custom

import app.goplus.lib.models.ApiResult
import app.goplus.lib.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, ApiResult<*>>(proxy) {

    override fun cloneImpl() = ResultCall(proxy.clone())

    override fun enqueueImpl(callback: Callback<ApiResult<*>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in HttpURLConnection.HTTP_OK until HttpURLConnection.HTTP_MULT_CHOICE) {
                val body = response.body()
                if (body is ApiResult<*>) {
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
                    Network.reValidateUser(code)
                    ApiResult.success(body, responseCode = code)
                }
            } else {
                ApiResult.error(message = response.message(), responseCode = code)
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = ApiResult.error(data = null, message = t.message ?: "", throwable = t)
            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })
}
