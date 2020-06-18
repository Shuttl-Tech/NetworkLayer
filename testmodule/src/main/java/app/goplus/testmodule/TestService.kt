package app.goplus.testmodule

import app.goplus.lib.annotation.Retry
import app.goplus.lib.annotation.RetryPolicyType
import app.goplus.lib.models.ApiResult
import retrofit2.http.GET

interface TestService {

    @GET("echo/get/json")
    suspend fun getTestData(): ApiResult<TestModal>

    @GET("echo/get/json/")
    @Retry(retryPolicy = RetryPolicyType.linear, retryCount = 3)
    suspend fun getTestDataRetry(): ApiResult<TestModal>
}