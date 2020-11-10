package com.example.luigi.repository

import com.example.luigi.api.RetrofitInstance
import com.example.luigi.model.Restaurant
import retrofit2.Retrofit

class Repository {

    suspend fun  getRestaurant() : Restaurant{
        return RetrofitInstance.api.getRestaurant()
    }
}