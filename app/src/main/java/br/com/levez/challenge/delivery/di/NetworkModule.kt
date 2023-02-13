package br.com.levez.challenge.delivery.di

import android.content.Context
import br.com.levez.challenge.delivery.BuildConfig
import br.com.levez.challenge.delivery.network.api.LocalityApi
import br.com.levez.challenge.delivery.network.interceptor.CacheInterceptor
import br.com.levez.challenge.delivery.network.interceptor.OfflineInterceptor
import br.com.levez.challenge.delivery.network.manager.INetworkManager
import br.com.levez.challenge.delivery.network.manager.NetworkManager
import java.io.File
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val CACHE_DIR_NAME = "response-cache"
private const val CACHE_SIZE_IN_MB = 10485760L // 10MB

val networkModule = module {
    single { NetworkManager.getInstance(get()) }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideLocalityApi(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun provideOkHttpClient(context: Context, networkManager: INetworkManager): OkHttpClient {
    val responseCacheDir = File(context.cacheDir, CACHE_DIR_NAME)
    val cache = Cache(responseCacheDir, CACHE_SIZE_IN_MB)

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    return OkHttpClient().newBuilder()
        .cache(cache)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(OfflineInterceptor(networkManager))
        .addNetworkInterceptor(CacheInterceptor())
        .build()
}

fun provideLocalityApi(retrofit: Retrofit): LocalityApi =
    retrofit.create(LocalityApi::class.java)
