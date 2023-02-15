package br.com.levez.challenge.delivery.network.interceptor

import br.com.levez.challenge.delivery.network.manager.INetworkManager
import java.util.concurrent.TimeUnit
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class OfflineInterceptor(private val networkManager: INetworkManager) : Interceptor {

    companion object {
        private const val CACHE_MAX_STALE_IN_DAYS = 30
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (networkManager.isOnline().not()) {
            val cacheControl = CacheControl.Builder()
                .maxStale(CACHE_MAX_STALE_IN_DAYS, TimeUnit.DAYS)
                .onlyIfCached()
                .build()

            request = request.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .removeHeader("Pragma")
                .build()
        }
        return chain.proceed(request)
    }
}
