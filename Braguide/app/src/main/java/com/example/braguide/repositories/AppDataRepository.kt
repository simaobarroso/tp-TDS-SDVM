package com.example.braguide.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.braguide.model.Contact
import com.example.braguide.model.GuideDatabase
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

import com.example.braguide.model.AppData
import com.example.braguide.model.AppDataDAO
import com.example.braguide.model.AppDataAPI
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


class AppDataRepository(application: Application) {
    var appDataDAO: AppDataDAO
    var appData: Flow<AppData>

    private val backendURL = "https://64f-193-137-92-72.ngrok-free.app/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(backendURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: AppDataAPI by lazy {
        retrofit.create(AppDataAPI::class.java)
    }


    init {
        val scope = CoroutineScope(SupervisorJob())
        val database : GuideDatabase = GuideDatabase.getDatabase(application, scope)

        appDataDAO = database.appDataDAO()
        appData = flow {
            appDataDAO.appData.collect { localData : AppData?->
                if (localData != null) {
                    emit(localData)
                } else {
                    makeRequest()
                }
            }
        }

    }

    @Throws(IOException::class)
    private fun makeRequest() {
        val call: Call<List<AppData>> = retrofitService.appInfo

        call.enqueue(object : Callback<List<AppData>> {
            override fun onResponse(call: Call<List<AppData>>, response: Response<List<AppData>>) {
                response.body()?.let { body ->
                    if (response.isSuccessful) {
                        runBlocking {
                            insert(body[0])
                        }
                    } else {
                        Log.e("main", "onFailure: " + response.errorBody())
                    }
                }
            }

            override fun onFailure(call: Call<List<AppData>>, t: Throwable) {
                Log.e("main", "onFailure: " + t.message)
                Log.e("main", "message: " + t.cause)
            }
        })
    }

    fun insert(app : AppData) {
        InsertAsyncTask(appDataDAO).execute(app)
    }


    class InsertAsyncTask(private val appDataDAO: AppDataDAO) {

        fun execute(app: AppData) {
            CoroutineScope(Dispatchers.IO).launch {
                appDataDAO.insert(app)
            }
        }
    }

    fun getAppInfo(): LiveData<AppData?> {
        return appData.asLiveData()
    }

    val contacts: Flow<List<Contact>>
        get() = appDataDAO.getContacts("BraGuide")


}