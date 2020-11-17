package com.example.luigi.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.luigi.model.Restaurant

//TODO: PROFILE PIC
@Entity(tableName = "user_table")
data class EntityUser (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var passwordHash : String,
    var name : String,
    var address : String,
    var phone_number : String,
    var email : String
)
