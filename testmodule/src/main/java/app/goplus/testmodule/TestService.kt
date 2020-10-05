package app.goplus.testmodule

import app.goplus.lib.annotation.Retry
import app.goplus.lib.annotation.RetryPolicyType
import app.goplus.lib.models.ApiResult
import retrofit2.http.GET

interface TestService {

    @GET("https://international-backend.bangkok.shuttltech.com/api/v3/home_cards")
    suspend fun getTestData(): ApiResult<TestModal>

    @GET("echo/get/json/")
    @Retry(retryPolicy = RetryPolicyType.linear, retryCount = 3)
    suspend fun getTestDataRetry(): ApiResult<TestModal>
}