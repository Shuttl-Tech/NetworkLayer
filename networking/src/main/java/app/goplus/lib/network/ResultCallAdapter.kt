package app.goplus.lib.network

import app.goplus.lib.v2.models.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResultCallAdapter(private val responseType: Type) : CallAdapter<Type, Call<ApiResult<*>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<Type>): Call<ApiResult<*>> = ResultCall(call)
}