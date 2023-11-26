package com.example.platform_education

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("/users/login")
    suspend fun login(
        @Body loginRequest:LoginRequest
    ): Response<LoginResponse>
    @GET("/users/")
    fun getUsers(): Call<List<Etudiant>>

    @POST("/users/posts")
    suspend fun createPost(@Body etudiant: Etudiant): Response<ResponseBody>
    @PUT("/users/update/{userId}")
    suspend fun updateEtudiantState(@Path("userId") userId: Int, @Body updatedEtudiant: Etudiant): Response<ResponseBody>
    @PUT("/users/update_password/{userId}")
    suspend fun changePassword(@Path("userId") userId: String, @Body changePasswordRequest: ChangePasswordRequest): Response<ResponseBody>
}