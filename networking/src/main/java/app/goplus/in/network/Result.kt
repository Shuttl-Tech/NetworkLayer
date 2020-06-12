package app.goplus.`in`.network

import app.goplus.`in`.v2.models.WarningDTO

data class Result<T>(
        val data: T? = null,
        val success: Boolean? = data != null,
        val cached: Boolean? = null,
        val errorCode: String? = null,
        val responseCode: Int? = 0,
        val message: String? = null,
        val title: String? = null,
        val warningDTO: WarningDTO? = null,
        val throwable: Throwable? = null) {
    companion object {
        fun <T> success(data: T?,title: String? = null, message: String? = null): Result<T> {
            return Result(data, title = title, message = message)
        }

        fun <T> error(data: T? = null, message: String?, errorCode: String? = null, responseCode: Int? = 0, warningDTO: WarningDTO? = null, throwable: Throwable? = null): Result<T> {
            return Result(data = data, success = false, errorCode = errorCode, responseCode = responseCode, message = message, warningDTO = warningDTO, throwable = throwable)
        }
    }
}