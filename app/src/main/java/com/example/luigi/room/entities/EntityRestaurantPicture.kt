package com.example.luigi.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_pictures")
data class EntityRestaurantPicture (
    @PrimaryKey(autoGenerate = false)
    var restaurantPictureId : Int,
    var restaurantId : Int,
    var userId : Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image : ByteArray?
)