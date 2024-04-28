package com.example.braguide.model

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserAPI {

    @GET("/user")
    fun getUser(@Header("Cookie") cookie : String): Call<User>

    @POST("login")
    fun login(@Body login : JsonObject): Call<User>
    @POST("logout")
    fun logout(@Header("Content-Range")cr: String?): Call<User>
}