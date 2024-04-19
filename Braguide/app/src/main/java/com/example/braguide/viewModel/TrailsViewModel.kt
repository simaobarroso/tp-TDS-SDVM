package com.example.braguide.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.braguide.model.Trail
import com.example.braguide.repositories.TrailRepository


class TrailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TrailRepository
    var allTrails: LiveData<List<Trail>>

    init {
        repository = TrailRepository(application)
        allTrails = repository.allTrails.asLiveData()
    }
}