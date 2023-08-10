package com.example.refit.di

import com.example.refit.BuildConfig
import com.example.refit.data.network.NetworkInterceptor
import com.example.refit.data.network.api.ChatApi
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkNodeModule = module {
    single(named("node")) {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .addNetworkInterceptor(NetworkInterceptor())
            .addInterceptor(Interceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().build()
                )
            })
            .build()
    }

    single<Retrofit>(named("node")) {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(get(named("node")))
            .baseUrl(BuildConfig.SUB_URL)
            .build()
    }

    single<ChatApi> {
        get<Retrofit>(named("node")).create(ChatApi::class.java)
    }
}