package app.goplus.testmodule

import android.content.Context
import androidx.multidex.MultiDexApplication
import app.goplus.lib.network.ModuleDependency
import app.goplus.lib.network.Network
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Interceptor

class TestApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Network.init(object : ModuleDependency {
            override fun getAppContext(): Context {
                return applicationContext
            }

            override fun getExtraInterceptors(): List<Interceptor> {
                return listOf(ChuckerInterceptor(applicationContext))
            }

            override fun getBaseUrl(type: String): String {
                return "https://reqbin.com/"
            }

            override fun getGoogleKeys(): String? {
                return null
            }

            override fun getHeaders(): HashMap<String, String>? {
                return null
            }

            override fun reValidateUer(code: Int) {
            }
        })
    }
}