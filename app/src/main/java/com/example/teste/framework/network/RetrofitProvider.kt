package com.example.teste.framework.network

import android.util.Log
import com.example.teste.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpLog())
            .build()
    }

    inline fun <reified T> getApiService(): T {
        return getRetrofit().create(T::class.java)
    }

    fun httpLog(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(65, TimeUnit.SECONDS)
            .readTimeout(65, TimeUnit.SECONDS)
            .writeTimeout(65, TimeUnit.SECONDS)
        httpClientBuilder.addInterceptor(interceptor)

        return httpClientBuilder.build()
    }
}