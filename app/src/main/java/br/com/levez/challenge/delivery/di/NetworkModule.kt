package br.com.levez.challenge.delivery.di

import br.com.levez.challenge.delivery.BuildConfig
import br.com.levez.challenge.delivery.network.LocalityApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideLocalityApi(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient().newBuilder().build()

fun provideLocalityApi(retrofit: Retrofit): LocalityApi =
    retrofit.create(LocalityApi::class.java)
