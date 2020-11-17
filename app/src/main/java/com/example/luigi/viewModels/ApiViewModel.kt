package com.example.luigi.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luigi.model.CityNames
import com.example.luigi.model.CityRestaurants
import com.example.luigi.model.Restaurant
import com.example.luigi.repository.ApiRepository
import com.example.luigi.room.entities.EntityRestaurant
import kotlinx.coroutines.launch

class ApiViewModel(private val repository: ApiRepository): ViewModel() {
    val restaurants : MutableLiveData<List<EntityRestaurant>> = MutableLiveData()
    val cityNames : MutableLiveData<CityNames> = MutableLiveData()



    //get the restaurants of a city
    suspend fun getCityRestaurants(city: String){
        viewModelScope.launch {
            val apiResult = repository.getCityRestaurants(city)
            restaurants.value = CityRestaurantsToEntityRestaurants(apiResult)
        }


    }

    //Get city names and insert into the database
     private suspend fun getCityNames() {
        viewModelScope.launch {
            cityNames.value = repository.getCityNames()
        }

    }

    /*
    Preload data what will be displayed
    on the MainMenuFragment at startup
     */
    fun preLoadData(){
        viewModelScope.launch{
            getCityNames()
            getCityRestaurants("New York")
        }


    }

    // ********** PRIVATE FUNCTIONS **********
    fun CityRestaurantsToEntityRestaurants(apiResult: CityRestaurants) : List<EntityRestaurant>{
        val list = mutableListOf<EntityRestaurant>()

        for (i in 0 until apiResult.restaurants.size){
            val restaurant = apiResult.restaurants[i]
            val entityRestaurant = EntityRestaurant(
                    0,
                    restaurant.name,
                    restaurant.address,
                    restaurant.city,
                    restaurant.state,
                    restaurant.area,
                    restaurant.postal_code,
                    restaurant.country,
                    restaurant.phone,
                    restaurant.lat,
                    restaurant.lng,
                    restaurant.price,
                    restaurant.reserve_url,
                    restaurant.mobile_reserve_url,
                    restaurant.mobile_reserve_url,
                    "",
                    apiResult.current_page
            )
            list.add(entityRestaurant)
        }
        return list
    }
}