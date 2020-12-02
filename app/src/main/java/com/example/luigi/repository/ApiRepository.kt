package com.example.luigi.repository

import com.example.luigi.api.RetrofitInstance
import com.example.luigi.model.CityRestaurants

class ApiRepository {



    suspend fun getCityRestaurants(city: String) : CityRestaurants{
        return RetrofitInstance.api.getCityRestaurants(city)
    }

    suspend fun getCityNames() : List<String>{
        val result = RetrofitInstance.api.getCityNames()

        return result.cities
    }
}