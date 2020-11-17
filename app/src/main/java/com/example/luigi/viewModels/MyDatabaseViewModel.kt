package com.example.luigi.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.luigi.model.CityRestaurants
import com.example.luigi.repository.MyDatabaseRepository
import com.example.luigi.room.entities.EntityUser
import com.example.luigi.room.MyDatabase
import com.example.luigi.room.entities.EntityRestaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent

class MyDatabaseViewModel (application: Application): AndroidViewModel (application) {
    private val readAllData: LiveData<List<EntityUser>>
    private val repository: MyDatabaseRepository
    var useDatabase : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var useApi : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var restaurants : MutableLiveData<List<EntityRestaurant>> = MutableLiveData<List<EntityRestaurant>>()

    init {
        val userDao = MyDatabase.getDatabase(application).userDao()
        repository = MyDatabaseRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(entityUser: EntityUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(entityUser)
        }
    }

    fun getUser(email: String, passwordHash: String): EntityUser? {
        return repository.getUser(email, passwordHash)
    }

    fun InsertRestaurants(restaurants: List<EntityRestaurant>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRestaurants(restaurants)
        }
    }

    fun getCount(city: String, page: Int){
        viewModelScope.launch {
            var count = repository.getCount(city, page)
            if (count > 0 ){
                useDatabase.value = true
            }
            else{
                useApi.value = true
            }
        }
    }

    fun getRestaurants(city: String, page: Int){
        viewModelScope.launch {
            restaurants.value = repository.getRestaurants(city, page)
        }
    }
}