package com.example.platform_education

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("authToken") val authToken: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("user_id") val user_id: String?,
    @SerializedName("userType") val userType: String?,

    val responseData: List<Etudiant>?
){
    fun getFirstUsername(): String? {
        println("responseData: $responseData")
        return responseData?.firstOrNull()?.Name
    }
}