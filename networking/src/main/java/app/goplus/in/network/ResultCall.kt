package app.goplus.`in`.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, Result<*>>(proxy) {

    override fun cloneImpl() = ResultCall(proxy.clone())

    override fun enqueueImpl(callback: Callback<Result<*>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in HttpURLConnection.HTTP_OK until HttpURLConnection.HTTP_MULT_CHOICE) {
                val body = response.body()
                if (body is Result<*>) {
                    // creating new result for the responses where data is sent but isSuccess is false
                    Result(data = body.data, cached = body.cached, message = body.message, throwable = null)
                } else {
                    Result.success(body)
                }
            } else {
                Result.error(message = response.message(), responseCode = code)
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = Result.error(data = null, message = t.message ?: "", throwable = t)
            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })
}
