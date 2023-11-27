package com.example.platform_education

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("/users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("/users/")
    fun getUsers(): Call<List<Etudiant>>

    @DELETE("/users/delete/{numInscrit}")
    fun deleteEtudiant(@Path("numInscrit") numInscrit: Int): Call<ResponseBody>

    @POST("/users/posts")
    suspend fun createPost(@Body etudiant: Etudiant): Response<ResponseBody>

    @PUT("/users/update/{userId}")
    suspend fun updateEtudiantState(
        @Path("userId") userId: Int,
        @Body updatedEtudiant: Etudiant
    ): Response<ResponseBody>

    @GET("/users/{NumInscrit}")
    fun getUserById(@Path("NumInscrit") NumInscrit: Int): Call<Etudiant>

    @PUT("/users/update/{numInscrit}")
    fun updateUser(
        @Path("numInscrit") numInscrit: Int,
        @Body updatedUser: Etudiant
    ): Call<Void>

    @PUT("/users/update_password/{userId}")
    suspend fun changePassword(
        @Path("userId") userId: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ResponseBody>

    @GET("/enseignants/")
    fun getEnseignants(): Call<List<Enseignant>>

    @PUT("/enseignants/update/{id}")
    fun updateEnseignant(@Path("id") id: Int, @Body enseignant: Enseignant): Call<Void>

    @GET("/enseignants/{id}")
    fun getEnseignantById(@Path("id") enseignantId: Int): Call<Enseignant>

    @DELETE("/enseignants/delete/{id}")
    fun deleteEnseignant(@Path("id") id: Int): Call<ResponseBody>

    @POST("/enseignants/posts")
    suspend fun addEnseignant(@Body enseignant: Enseignant): Response<Enseignant>
}
