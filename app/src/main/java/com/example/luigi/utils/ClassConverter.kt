package com.example.luigi.utils

import com.example.luigi.model.FavoriteWithPicture
import com.example.luigi.model.RestaurantWithPicture
import com.example.luigi.room.entities.EntityFavorite
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
                null
            )
            return model
        }

        fun listFavoriteToWithPicture(favorites : List<EntityFavorite>) : MutableList<FavoriteWithPicture>{
            val list = mutableListOf<FavoriteWithPicture>()
            for (favorite in favorites){
                list.add(favoriteToWithPicture(favorite))
            }
            return list
        }

        fun favoriteToWithPicture(favorite : EntityFavorite) : FavoriteWithPicture{
            val withPicture = FavoriteWithPicture(
                favorite.id,
                favorite.ownerId,
                favorite.restaurantId,
                favorite.name,
                favorite.address,
                favorite.city,
                favorite.state,
                favorite.area,
                favorite.postal_code,
                favorite.country,
                favorite.phone,
                favorite.lat,
                favorite.lng,
                favorite.price,
                favorite.reserve_url,
                favorite.mobile_reserve_url,
                favorite.image_url,
                null
            )
            return withPicture
        }

        fun favoriteWithPictureToFavorite(withPicture: FavoriteWithPicture) : EntityFavorite{
            val entity = EntityFavorite(
                withPicture.id,
                withPicture.ownerId,
                withPicture.restaurantId,
                withPicture.name,
                withPicture.address,
                withPicture.city,
                withPicture.state,
                withPicture.area,
                withPicture.postal_code,
                withPicture.country,
                withPicture.phone,
                withPicture.lat,
                withPicture.lng,
                withPicture.price,
                withPicture.reserve_url,
                withPicture.mobile_reserve_url,
                withPicture.image_url
            )
            return entity
        }

        fun restaurantWithPictureToEntityFavorite(restaurant : RestaurantWithPicture, userId : Int) : EntityFavorite{
            val entityFavorite = EntityFavorite(
                0,
                userId,
                restaurant.id,
                restaurant.name,
                restaurant.address,
                restaurant.city,
                restaurant.state,
                restaurant.area,
                restaurant.postal_code,
                restaurant.country,
                restaurant.phone,
                restaurant.lat,
                restaurant.lng,
                restaurant.price,
                restaurant.mobile_reserve_url,
                restaurant.mobile_reserve_url,
                restaurant.image_url
            )

            return entityFavorite
        }
    }
}