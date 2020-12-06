package com.example.luigi.model

import android.graphics.Bitmap


data class RestaurantPicture (
    var restaurantPictureId : Int,
    var restaurantId : Int,
    var userId : Int,
    var image : Bitmap
        )