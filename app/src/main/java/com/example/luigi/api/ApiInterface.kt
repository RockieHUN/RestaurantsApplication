package com.example.luigi.api

import com.example.luigi.model.CityNames
import com.example.luigi.model.CityRestaurants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("restaurants")
    suspend fun getCityRestaurants(@Query("city") city: String) : CityRestaurants

    @GET("cities")
    suspend fun getCityNames() : CityNames
}