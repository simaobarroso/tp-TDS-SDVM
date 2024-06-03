package com.example.braguide.repositories

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.braguide.model.GuideDatabase
import com.example.braguide.model.TrailMetrics
import com.example.braguide.model.TrailMetricsDAO
import com.example.braguide.model.Trip
import com.example.braguide.model.User
import com.example.braguide.model.UserAPI
import com.example.braguide.model.UserDAO
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.stream.Collectors


class UserRepository(application: Application) {
    var userDAO: UserDAO
    var user: Flow<User>
    lateinit var trailMetricsDAO : TrailMetricsDAO


    private val backendURL = "https://64f-193-137-92-72.ngrok-free.app/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(backendURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val retrofitService: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }


    private var lastUser: String? = null

    init {
        val scope = CoroutineScope(SupervisorJob())
        val database : GuideDatabase = GuideDatabase.getDatabase(application, scope)

        userDAO = database.userDAO()
        trailMetricsDAO = database.trailMetricsDAO()

        user = flow {
            val sharedPreferences = application.applicationContext.getSharedPreferences(
                "BraguidePreferences",
                Context.MODE_PRIVATE
            )

            val cookies = sharedPreferences.getString("cookies", "")
            updateUserAPI(cookies, object : LoginCallback {
                override fun onLoginSuccess() {}
                override fun onLoginFailure() {}
            }, application.applicationContext)
            lastUser = sharedPreferences.getString("lastUser", "")

            if (lastUser == null || lastUser == "") {
                emit(User("", "loggedOff",null,null,null))
            }

            lastUser?.let {
                userDAO.getUserByUsername(it).collect { localUser ->
                    if (lastUser == null || lastUser == "") {
                        emit(User("", "loggedOff",null,null,null))
                    }
                    emit(localUser)
                }
            }
        }
    }

    fun updateUserAPI(cookies: String?, callback: LoginCallback, context: Context) {
        if (cookies !== "") {
            val scope = CoroutineScope(SupervisorJob())

            Log.e("DEBUG", "Cookies:$cookies")
            val call = retrofitService.getUser(cookies!!)
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        val responseBody = response.body().toString()
                        Log.e("Retrofit", "Response Body: $responseBody")
                        scope.launch {
                            if (user != null) {
                                insert(user)
                            }
                        }
                        lastUser = user!!.username
                        val sharedPreferences =
                            context.getSharedPreferences("BraguidePreferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("lastUser", lastUser).apply()
                        callback.onLoginSuccess()
                    } else {
                        Log.e("Retrofit", "Unsuccessful Response: $response")
                        callback.onLoginFailure()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("Retrofit", "Response error:" + t.message)
                    callback.onLoginFailure()
                }
            })
        }
    }

    @Throws(IOException::class)
    fun makeLoginRequest(
        username: String?,
        password: String?,
        context: Context,
        callback: LoginCallback
    ) {
        val body = JsonObject()
        body.addProperty("username", username)
        body.addProperty("email", "")
        body.addProperty("password", password)
        val call = retrofitService.login(body)
        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful && response.body() != null) {
                    // Store the cookies
                    val headers: Headers = response.headers()
                    val cookies: List<String?> = headers.values("Set-Cookie").stream().map { e ->
                        e.split(";")[0]
                    }.collect(Collectors.toList())
                    if (cookies.isNotEmpty()) { //Insert cookie into SharedPreferences
                        val cookieString = TextUtils.join(";", cookies)
                        val sharedPreferences =
                            context.getSharedPreferences("BraguidePreferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("cookies", cookieString).apply()
                        updateUserAPI(cookieString, callback, context)
                    }
                } else {
                    Log.e("main", "onFailure: " + response.errorBody())
                    callback.onLoginFailure()
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.e("main", "onFailure: " + t.message)
                Log.e("main", "message: " + t.cause)
                callback.onLoginFailure()
            }
        })
    }

    interface LoginCallback {
        fun onLoginSuccess()
        fun onLoginFailure()
    }

    @Throws(IOException::class)
    fun makeLogOutRequest(context: Context, callback: LogoutCallback) {
        var sharedPreferences =
            context.getSharedPreferences("BraguidePreferences", Context.MODE_PRIVATE)
        val storedCookieString = sharedPreferences.getString("cookies", "")
        val call = retrofitService.logout(storedCookieString)
        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    lastUser = ""
                    sharedPreferences =
                        context.getSharedPreferences("BraguidePreferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("cookies", "").apply()
                    sharedPreferences.edit().putString("lastUser", "").apply()
                    Log.e("main", "logged out successfully:")
                    callback.onLogoutSuccess()
                } else {
                    Log.e("main", "onFailure: " + response.errorBody())
                    callback.onLogoutFailure()
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.e("main", "onFailure: " + t.message)
                callback.onLogoutFailure()
            }
        })
    }


    interface LogoutCallback {
        fun onLogoutSuccess()
        fun onLogoutFailure()
    }


    @WorkerThread
    fun insert(user: User) {
        userDAO.insert(user)
    }


    fun getTrailMetricsById(id: Int): LiveData<TrailMetrics> {
        return trailMetricsDAO.getMetricsById(id)
    }

    fun getTrailMetrics(): LiveData<List<TrailMetrics>> {
        return user.asLiveData().switchMap { user ->
            if (user == null) {
                return@switchMap MutableLiveData<List<TrailMetrics>>(emptyList())
            } else {
                return@switchMap trailMetricsDAO.allMetrics
            }
        }
    }

    fun addTrailMetrics(trip: Trip) {
        val trailMetrics = trip.finish()
        InsertTrailMetricsAsyncTask(trailMetricsDAO).execute(trailMetrics)
    }

    class InsertTrailMetricsAsyncTask(private val trailMetricsDAO: TrailMetricsDAO) {

        fun execute(trailMetrics : TrailMetrics) {
            CoroutineScope(Dispatchers.IO).launch {
                trailMetricsDAO.insert(trailMetrics)
            }
        }
    }
}