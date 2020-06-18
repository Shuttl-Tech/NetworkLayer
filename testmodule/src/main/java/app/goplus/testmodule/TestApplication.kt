package app.goplus.testmodule

import android.app.Application
import android.content.Context
import app.goplus.lib.network.ModuleDependency
import app.goplus.lib.network.Network

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Network.init(object : ModuleDependency {
            override fun getAppContext(): Context {
                return applicationContext
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