package com.example.braguide.model


import retrofit2.Call
import retrofit2.http.GET
import java.io.IOException


interface AppDataAPI {
    @get:Throws(IOException::class)
    @get:GET("app")
    val appInfo: Call<List<AppData>>
}