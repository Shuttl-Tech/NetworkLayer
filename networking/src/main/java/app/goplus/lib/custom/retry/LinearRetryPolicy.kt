package app.goplus.lib.custom.retry

import app.goplus.lib.custom.RetryPolicy
import retrofit2.Call
import retrofit2.Callback

class LinearRetryPolicy(private val retryMaxCount: Int) : RetryPolicy {
    var retryCount = 0
    override fun <T> retry(proxy: Call<T>, callback: Callback<T>): Boolean {
        if (retryCount++ < retryMaxCount) {
            val nextProxy = proxy.clone()
            nextProxy.enqueue(callback)
            return true
        }
        return false
    }
}
