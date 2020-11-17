package com.example.luigi.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luigi.model.CityNames
import com.example.luigi.model.CityRestaurants
import com.example.luigi.model.Restaurant
import com.example.luigi.repository.ApiRepository
import kotlinx.coroutines.launch

class ApiViewModel(private val repository: ApiRepository): ViewModel() {

    val response : MutableLiveData<Restaurant> = MutableLiveData()
    val restaurants : MutableLiveData<CityRestaurants> = MutableLiveData()
    val cityNames : MutableLiveData<CityNames> = MutableLiveData()

    //get a SINGLE restaurant
    fun getRestaurant(){
        viewModelScope.launch {
            val restaurant = repository.getRestaurant()
            response.value = restaurant
        }
    }

    //get the restaurants of a city
    suspend fun getCityRestaurantsAsync(city: String){
        viewModelScope.launch {
            restaurants.value = repository.getCityRestaurants(city)
            Log.d("*****Restaurants******",restaurants.value.toString())
        }
    }

    //Get city names and insert into the database
    private suspend fun getCityNamesAsync() {
        viewModelScope.launch {
            cityNames.value = repository.getCityNames()
        }
    }

    /*
    Preload data what will be displayed
    on the MainMenuFragment at startup
     */
    fun preLoadData(){
        viewModelScope.launch {
            getCityNamesAsync()
            getCityRestaurantsAsync("New York")
        }
    }


}