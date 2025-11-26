package com.pokeshopv2.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val services = mutableMapOf<String, Any>()

    fun <T> getService(apiInterface: Class<T>, baseUrl: String): T {
        val serviceName = apiInterface.simpleName
        if (services.containsKey(serviceName)) {
            @Suppress("UNCHECKED_CAST")
            return services[serviceName] as T
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newService = retrofit.create(apiInterface)

        services[serviceName] = newService as Any

        return newService
    }
}