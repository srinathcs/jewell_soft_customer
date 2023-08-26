package com.sgs.manthara.jewelRetrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

class JewelSoftRetro {
    companion object {
        private val sample by lazy {

            val gson = GsonBuilder().setLenient().create()
            val loggingMXBean = HttpLoggingInterceptor()
            loggingMXBean.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor(loggingMXBean)
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            retrofit2.Retrofit.Builder().baseUrl(Constant.BASE_URL).addConverterFactory(
                GsonConverterFactory.create(gson)
            ).client(client).build()
        }

        val api: JewelInterface by lazy {
            sample.create(JewelInterface::class.java)
        }
    }
}