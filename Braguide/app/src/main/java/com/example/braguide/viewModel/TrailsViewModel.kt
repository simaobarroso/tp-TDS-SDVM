package com.example.braguide.viewModel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.braguide.model.EdgeTip
import com.example.braguide.model.Trail
import com.example.braguide.repositories.TrailRepository


class TrailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TrailRepository
    var allTrails: LiveData<List<Trail>>

    init {
        repository = TrailRepository(application)
        Log.e("DEBUG", "New user Trails Model")
        allTrails = repository.allTrails.asLiveData()
    }


    fun getTrailById(id: Int): LiveData<Trail> {
        return repository.getTrailById(id)
    }

    fun getTrailsById(ids: List<Int>): LiveData<List<Trail>> {
        return repository.getTrailsById(ids)
    }

    fun getPinsById(ids: List<Int>): LiveData<List<EdgeTip>> {
        return repository.getPinsById(ids)
    }
}

class TrailsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TrailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrailsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}