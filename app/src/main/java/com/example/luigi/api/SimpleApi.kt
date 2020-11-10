package com.example.luigi.api

import com.example.luigi.model.Restaurant
import retrofit2.http.GET

interface SimpleApi {

    @GET("restaurants/107257")
    suspend fun getRestaurant(): Restaurant
}