package com.example.braguide.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.example.braguide.model.EdgeTip
import com.example.braguide.model.GuideDatabase
import com.example.braguide.model.Trail
import com.example.braguide.model.TrailAPI
import com.example.braguide.model.TrailDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TrailRepository (application: Application){

    var trailDao: TrailDAO
    var allTrails: Flow<List<Trail>>

    private val backendURL = "https://64f-193-137-92-72.ngrok-free.app/"

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



    fun insert(trails: List<Trail>) {
        InsertAsyncTask(trailDao).execute(trails)
    }

    fun getTrailById(id : Int) : LiveData<Trail> {
        return trailDao.getTrailById(id).asLiveData()
    }

    fun getTrailsById(ids : List<Int>) : LiveData<List<Trail>> {
        return trailDao.getTrailsById(ids).asLiveData()
    }

    fun getPinsById(ids: List<Int>): LiveData<List<EdgeTip>> {
        val mediatorLiveData = MediatorLiveData<List<EdgeTip>>()
        val liveData: LiveData<List<Trail>> = trailDao.trails.asLiveData()
        mediatorLiveData.addSource(
            liveData
        ) { trails: List<Trail> ->
            val filteredPins: MutableList<EdgeTip> =
                ArrayList()
            var appended: Boolean
            for (id in ids) {
                appended = false
                for (trail in trails) {
                    if (!appended) {
                        for (pin in trail.getRoute()) {
                            if (!appended && pin.id == id) {
                                filteredPins.add(pin)
                                appended = true
                            }
                        }
                    }
                }
            }
            mediatorLiveData.postValue(filteredPins)
        }
        return mediatorLiveData
    }


    class InsertAsyncTask(private val trailDAO: TrailDAO) {

        fun execute(trails: List<Trail>) {
            CoroutineScope(Dispatchers.IO).launch {
                trailDAO.insert(trails)
            }
        }
    }

}