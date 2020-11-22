package com.example.luigi.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_table")
data class EntityRestaurant(
        @PrimaryKey(autoGenerate = true)
        var id : Int,
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
        val image_url: String,
        var timestamp: String,
        val page: Int
)