package com.service.codingtest.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.service.codingtest.model.response.JsonData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ImageAPI {

    @GET("search/users")
    suspend fun getAPI(
        @Query("q") query: String? = null,
        @Query("page") page: Int? = 0)
            : JsonData

    companion object {
        fun create(): ImageAPI {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level =
                if (MLog.displayLog) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder().apply {
                baseUrl(Constant.URL_HOME)
                client(client)
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                addConverterFactory(GsonConverterFactory.create())
            }.build().create(ImageAPI::class.java)
        }
    }
}