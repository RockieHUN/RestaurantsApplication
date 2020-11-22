package com.example.luigi.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class EntityCity (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var name : String
)