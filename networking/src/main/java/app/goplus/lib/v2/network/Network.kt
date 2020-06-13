package app.goplus.lib.v2.network

import android.content.Context

class Network {
    companion object {
        internal var context: Context? = null
        private lateinit var moduleDependency: ModuleDependency
        @JvmOverloads
        fun init(dependency: ModuleDependency) {
            moduleDependency = dependency
            context = dependency.getAppContext()
        }

        fun getBaseUrl(type: String): String {
            return return moduleDependency.getBaseUrl(type)
        }

        fun getHeaders(): HashMap<String, String> {
            return moduleDependency.getHeaders()
        }

        fun getStringFromRes(resId: Int) = context?.getString(resId)

        fun reValidateUser(context: Context, code: Int) {
            moduleDependency.reValidateUer(context, code)
        }

        fun getGoogleKey(): String? {
            return moduleDependency.getGoogleKeys()
        }
    }
}

interface ModuleDependency {
    fun getBaseUrl(type: String): String
    fun getHeaders(): HashMap<String, String>
    fun reValidateUer(context: Context, code: Int)
    fun getGoogleKeys(): String?
    fun getAppContext(): Context
}