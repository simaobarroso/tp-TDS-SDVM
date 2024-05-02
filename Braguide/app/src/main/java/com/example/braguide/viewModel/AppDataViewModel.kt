package com.example.braguide.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.braguide.model.AppData
import com.example.braguide.model.Trail
import com.example.braguide.repositories.AppDataRepository
class AppDataViewModel(application: Application) : AndroidViewModel(application){

    private val repository: AppDataRepository
    var appData: LiveData<AppData>

    init {
        repository = AppDataRepository(application)
        appData = repository.appData.asLiveData()
    }

}

class AppDataViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AppDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppDataViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}