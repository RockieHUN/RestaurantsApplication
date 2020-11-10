package com.example.luigi.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.luigi.model.Restaurant

//TODO: PROFILE PIC
@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val address : String,
    val phone_number : String,
    val email : String,
    val favorites : List<Restaurant>
)
