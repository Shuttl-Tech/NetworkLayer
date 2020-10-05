package app.goplus.test.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import app.goplus.lib.network.HttpServiceGenerator
import app.goplus.testmodule.R
import app.goplus.testmodule.TestService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callApi(null)
    }

    fun callApi(view: View?) {
        CoroutineScope(Dispatchers.IO).launch {
            var service =
                HttpServiceGenerator.generateResultService(baseContext, TestService::class.java)

            var data = service.getTestData()

            Log.e("TestTag", "result is ${data.data?.success}")
        }
    }

    fun callApiRetry(view: View?) {
        CoroutineScope(Dispatchers.IO).launch {
            var service =
                HttpServiceGenerator.generateResultService(baseContext, TestService::class.java)

            var data = service.getTestDataRetry()

            Log.e("TestTag", "result is ${data.error?.message}")
        }
    }
}
