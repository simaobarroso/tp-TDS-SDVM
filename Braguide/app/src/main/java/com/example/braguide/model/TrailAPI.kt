package com.example.braguide.model

import retrofit2.Call
import retrofit2.http.GET

interface TrailAPI {
    @GET("trails")
    fun getTrails(): Call<List<Trail>>
}