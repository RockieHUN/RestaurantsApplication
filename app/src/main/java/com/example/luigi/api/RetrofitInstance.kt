package com.example.luigi.api

import com.example.luigi.utils.Constanst.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
                .baseUrl("https://ratpark-api.imok.space/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val api: ApiInterface by lazy{
        retrofit.create(ApiInterface :: class.java)
    }
}