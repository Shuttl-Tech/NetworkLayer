package app.goplus.lib.v2.models

data class ApiResult<T>(
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
        fun <T> success(data: T?,title: String? = null, message: String? = null): ApiResult<T> {
            return ApiResult(
                data,
                title = title,
                message = message
            )
        }

        fun <T> error(data: T? = null, message: String?, errorCode: String? = null, responseCode: Int? = 0, warningDTO: WarningDTO? = null, throwable: Throwable? = null): ApiResult<T> {
            return ApiResult(
                data = data,
                success = false,
                errorCode = errorCode,
                responseCode = responseCode,
                message = message,
                warningDTO = warningDTO,
                throwable = throwable
            )
        }
    }
}