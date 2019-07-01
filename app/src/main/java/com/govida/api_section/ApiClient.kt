/**
 * @Class :  ApiClient
 * @Usage : This class is used for providing retrofit api functionality to application
 * @Author : 1276
 */

package com.govida.api_section

import com.govida.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {
    private val BASE_URL = BuildConfig.BASE_URL
    private lateinit var retrofit: Retrofit

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        //.addInterceptor(AuthorizationInterceptor())
        .build()!!


    fun getClient(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}