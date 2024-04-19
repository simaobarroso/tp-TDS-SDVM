package com.example.braguide.model

import retrofit2.Call
import retrofit2.http.GET

interface UserAPI {

    @GET("users")
    fun getUsers(): Call<List<User>>
}