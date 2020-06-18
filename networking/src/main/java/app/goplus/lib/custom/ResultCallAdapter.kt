package app.goplus.lib.custom

import app.goplus.lib.models.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResultCallAdapter(
    private val responseType: Type,
    private val retryPolicy: RetryPolicy?
) : CallAdapter<Type, Call<ApiResult<*>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<Type>): Call<ApiResult<*>> = ResultCall(call, retryPolicy)
}