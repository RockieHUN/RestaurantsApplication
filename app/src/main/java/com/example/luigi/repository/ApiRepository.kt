package com.example.luigi.repository

import com.example.luigi.api.RetrofitInstance
import com.example.luigi.model.CityNames
import com.example.luigi.model.CityRestaurants
import com.example.luigi.model.Restaurant

class ApiRepository {



    suspend fun getCityRestaurants(city: String) : CityRestaurants{
        return RetrofitInstance.api.getCityRestaurants(city)
    }

    suspend fun getCityNames() : CityNames{
        return RetrofitInstance.api.getCityNames()
    }
}