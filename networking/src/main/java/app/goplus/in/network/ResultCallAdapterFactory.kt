package app.goplus.`in`.network

import app.goplus.`in`.v2.models.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?) =
            if (getRawType(returnType) == Call::class.java) {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                if (getRawType(callType) == ApiResult::class.java) {
                    ResultCallAdapter(callType)
                } else null
            } else null

    companion object {
        @JvmStatic
        fun create() = ResultCallAdapterFactory()
    }
}