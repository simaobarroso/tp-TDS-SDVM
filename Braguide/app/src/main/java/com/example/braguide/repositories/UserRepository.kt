package com.example.braguide.repositories

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.braguide.model.GuideDatabase
import com.example.braguide.model.User
import com.example.braguide.model.UserAPI
import com.example.braguide.model.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository(application: Application) {
/*
    var userDao : UserDAO
    var user: Flow<User>


    private val backendURL = "https://c14d-193-137-92-5.ngrok-free.app"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(backendURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }


    init {
        val scope = CoroutineScope(SupervisorJob())
        val database : GuideDatabase = GuideDatabase.getDatabase(application, scope)
        userDao = database.userDAO()
        user = flow {
            userDao.getUserByUsername().collect { localUser ->

            }
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }*/
}