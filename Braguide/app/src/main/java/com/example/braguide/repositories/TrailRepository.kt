package com.example.braguide.repositories

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.braguide.model.GuideDatabase
import com.example.braguide.model.Trail
import com.example.braguide.model.TrailAPI
import com.example.braguide.model.TrailDAO
import com.squareup.picasso.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TrailRepository (application: Application){

    var trailDao: TrailDAO
    var allTrails: Flow<List<Trail>>

    private val backendURL = "https://c14d-193-137-92-5.ngrok-free.app"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(backendURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: TrailAPI by lazy {
        retrofit.create(TrailAPI::class.java)
    }

    private fun makeRequest() {
        val call: Call<List<Trail>> = retrofitService.getTrails()
        call.enqueue(object : Callback<List<Trail>> {
            override fun onResponse(call: Call<List<Trail>>, response: Response<List<Trail>>) {
                response.body()?.let { body ->
                    if (response.isSuccessful) {
                        runBlocking {
                            insert(body)
                        }
                    } else {
                        Log.e("main", "onFailure: " + response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<List<Trail>>, t: Throwable) {
                Log.e("main", "onFailure: " + t.message)
            }
        })
    }

    init {
        val scope = CoroutineScope(SupervisorJob())
        val database : GuideDatabase = GuideDatabase.getDatabase(application, scope)
        trailDao = database.trailDAO()
        allTrails = flow {
            trailDao.trails.collect { localTrails ->
                // TODO: ADD cache validation logic
                if (localTrails.isNotEmpty()) {
                    emit(localTrails)
                } else {
                    makeRequest()
                }
            }
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(trail: List<Trail>) {
        trailDao.insert(trail)
    }
}