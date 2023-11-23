package com.example.platform_education

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.56.1:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}