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
import com.example.luigi.room.entities.EntityCity
import com.example.luigi.room.entities.EntityRestaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent

class MyDatabaseViewModel (application: Application): AndroidViewModel (application) {
    private val readAllData: LiveData<List<EntityUser>>
    private val repository: MyDatabaseRepository

    //the loaded data will be stored in these variables
    var restaurants : MutableLiveData<List<EntityRestaurant>> = MutableLiveData<List<EntityRestaurant>>()
    var cityNames : List<String> = listOf<String>()

    //used for determining from where the data should be loaded (SplashScreen)
    var useDatabase : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var useApi : MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    //used for communication between recycleView and Detail Fragment
    var position : Int = 0

    init {
        val userDao = MyDatabase.getDatabase(application).userDao()
        repository = MyDatabaseRepository(userDao)
        readAllData = repository.readAllData
    }

    // register the user in the database
    fun addUser(entityUser: EntityUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(entityUser)
        }
    }

    // get user from database (used at the login screen)
    fun getUser(email: String, passwordHash: String): EntityUser? {
        return repository.getUser(email, passwordHash)
    }

    // insert a list of restaurant to database
    fun InsertRestaurants(restaurants: List<EntityRestaurant>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRestaurants(restaurants)
        }
    }

    /*
    Check if the restaurants with the same city name and page number
    exist in the database. For this we will return the count.
     */
    fun getCount(city: String, page: Int){
        viewModelScope.launch {
            val count = repository.getCount(city, page)
            if (count > 0 ){
                useDatabase.value = true
            }
            else{
                useApi.value = true
            }
        }
    }

    /*
    Load restaurants of the given city and page
     */
    fun loadRestaurantsFromDatabase(city: String, page: Int){
        viewModelScope.launch {
            restaurants.value = repository.getRestaurants(city, page)
        }
    }

    /*
     Delete every downloaded restaurants and
     data added by the user
     */
    fun deleteCache() {
        viewModelScope.launch {
            repository.deleteCache()
        }
    }

    //insert a list of cities to the database
    fun insertCities(cities : List<String>){
        viewModelScope.launch {
            repository.addCities(cities)
        }
    }

    //get the list of city names
    fun loadCityNamesFromDatabase(){
        viewModelScope.launch {
            cityNames = repository.getCityNames()
        }
    }

}