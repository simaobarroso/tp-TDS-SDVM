package com.example.braguide.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TrailAPI {
    @GET("trails")
    fun getTrails(): Call<List<Trail>>

    @GET("trail/{id}")
    fun getTrailById(@Path("id") id: Int): Call<Trail>
}