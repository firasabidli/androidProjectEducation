package com.example.platform_education

import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService {
    @GET("/login") // Replace with your actual login endpoint
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): LoginResponse
}