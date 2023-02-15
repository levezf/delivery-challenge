package br.com.levez.challenge.delivery.network.interceptor

import java.util.concurrent.TimeUnit
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {

    companion object {
        private const val CACHE_MAX_AGE_IN_DAYS = 7
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val cacheControl = CacheControl.Builder()
            .maxAge(CACHE_MAX_AGE_IN_DAYS, TimeUnit.DAYS)
            .build()

        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .removeHeader("Pragma")
            .build()
    }
}
