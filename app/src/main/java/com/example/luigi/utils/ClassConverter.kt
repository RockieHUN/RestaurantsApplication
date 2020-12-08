package com.example.luigi.utils

import com.example.luigi.model.RestaurantWithPicture
import com.example.luigi.room.entities.EntityRestaurant

class ClassConverter {

    companion object{

        fun listRestaurantToWithPicture(entityList: List<EntityRestaurant>) : MutableList<RestaurantWithPicture>{
            val list = mutableListOf<RestaurantWithPicture>()
            for (entity in entityList){
                list.add(restaurantToWithPicture(entity))
            }
            return list
        }

        fun restaurantToWithPicture(entity : EntityRestaurant) : RestaurantWithPicture{
            val model = RestaurantWithPicture(
                entity.id,
                entity.name,
                entity.address,
                entity.city,
                entity.state,
                entity.area,
                entity.postal_code,
                entity.country,
                entity.phone,
                entity.lat,
                entity.lng,
                entity.price,
                entity.reserve_url,
                entity.mobile_reserve_url,
                entity.image_url,
                entity.timestamp,
                entity.page,
                null
            )
            return model
        }
    }
}