package com.example.luigi.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.luigi.model.CityRestaurants
import com.example.luigi.repository.MyDatabaseRepository
import com.example.luigi.room.entities.EntityUser
import com.example.luigi.room.MyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyDatabaseViewModel (application: Application): AndroidViewModel (application){
    private val readAllData : LiveData<List<EntityUser>>
    private val repository : MyDatabaseRepository

    init {
        val userDao = MyDatabase.getDatabase(application).userDao()
        repository = MyDatabaseRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(entityUser: EntityUser){
        viewModelScope.launch (Dispatchers.IO){
            repository.addUser(entityUser)
        }
    }

    fun getUser(email : String, passwordHash : String): EntityUser?{
        return repository.getUser(email,passwordHash)
        }

    fun InsertRestaurants(restaurants : CityRestaurants){
       viewModelScope.launch (Dispatchers.IO){
           repository.addRestaurants(restaurants)
       } 
    }

    }
