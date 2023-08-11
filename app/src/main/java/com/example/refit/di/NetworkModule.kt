package com.example.refit.di

import android.net.Uri.Builder
import com.example.refit.data.network.NetworkInterceptor
import com.example.refit.data.network.api.ClosetApi
import com.example.refit.data.network.api.CommunityApi
import com.example.refit.data.network.api.MyPageApi
import com.example.refit.data.network.api.SignUpApi
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import com.example.refit.BuildConfig
import com.example.refit.data.network.NullOnEmptyConverterFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

val networkModule = module {
    single {
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

    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(get())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    //TODO(앞으로 생성할 API 인터페이스의 싱글톤 인스턴스는 여기서 생성해야 함)
    single<SignUpApi> {
        get<Retrofit>().create(SignUpApi::class.java)
    }

    single<ClosetApi> {
        get<Retrofit>().create(ClosetApi::class.java)
    }

    single<CommunityApi> {
        get<Retrofit>().create(CommunityApi::class.java)
    }
    single<MyPageApi> {
        get<Retrofit>().create(MyPageApi::class.java)
    }


}