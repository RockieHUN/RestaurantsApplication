package com.example.luigi.model

data class Restaurant (
    val id : Int,
    val name : String,
    val address : String,
    val city : String,
    val state : String,
    val area : String,
    val postal_code: Int,
    val country : String,
    val phone : String,
    val lat: Double,
    val lng : Double,
    val price : Double,
    val reserve_url : String,
    val mobile_reserve_url: String,
    val image_url: String

)

