package com.example.luigi.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.luigi.repository.Repository

class ApiViewModelFactory (private val repository: Repository): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApiViewModel(repository) as T
    }

}