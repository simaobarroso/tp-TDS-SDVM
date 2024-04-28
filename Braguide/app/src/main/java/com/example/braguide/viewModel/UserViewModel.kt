package com.example.braguide.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.braguide.model.User
import com.example.braguide.repositories.UserRepository
import java.io.IOException


class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    @get:Throws(IOException::class)
    var user: LiveData<User>

    init {
        repository = UserRepository(application)
        Log.e("DEBUG", "New user View Model")
        user = repository.user.asLiveData()
    }

    @Throws(IOException::class)
    fun login(username: String?, password: String?, context: Context?, callback: LoginCallback) {
        if (context != null) {
            repository.makeLoginRequest(username, password, context, object : UserRepository.LoginCallback {
                override fun onLoginSuccess() {
                    callback.onLoginSuccess()
                }

                override fun onLoginFailure() {
                    callback.onLoginFailure()
                }
            })
        }
    }

    interface LoginCallback {
        fun onLoginSuccess()
        fun onLoginFailure()
    }

    @Throws(IOException::class)
    fun logOut(context: Context?, callback: LogoutCallback) {
        if (context != null) {
            repository.makeLogOutRequest(context, object : UserRepository.LogoutCallback {
                override fun onLogoutSuccess() {
                    callback.onLogoutSuccess()
                }

                override fun onLogoutFailure() {
                    callback.onLogoutFailure()
                }
            })
        }
    }

    interface LogoutCallback {
        fun onLogoutSuccess()
        fun onLogoutFailure()
    }



    fun getDarkModePreference(context: Context): Boolean {
        val sharedPreferences =
            context.getSharedPreferences("BraguidePreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("darkModeSwitchState", true)
    }
}
class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}