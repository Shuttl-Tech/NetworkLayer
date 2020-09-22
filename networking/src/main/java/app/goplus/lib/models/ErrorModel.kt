package app.goplus.lib.models

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    val type: ErrorType? = null,
    val message: String? = null,
    val params: ErrorParams? = null
)

data class ErrorParams(
    val heading: String? = null,
    val cta: String? = null,
    val action: ErrorActions? = null,
    @SerializedName("display_type") val displayType: DisplayType? = null
)

enum class DisplayType {
    FULL_SCREEN, HALF_SCREEN, TOAST
}

enum class ErrorActions {
    REFRESH, SHOW_AVAILABLE, ENABLE_LOCATION, ENABLE_NETWORK
}

enum class ErrorType {
    AUTHENTICATION_ERROR, INVALID_PARAMS, LOCATION, INTERNAL_SERVER_ERROR, INVALID_OTP, INTERNET, GPS
}
