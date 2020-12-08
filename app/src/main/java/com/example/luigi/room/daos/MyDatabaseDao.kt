package com.example.luigi.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.luigi.room.entities.*

@Dao
interface MyDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(entityUser : EntityUser)

    @Query("Select * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<EntityUser>>

    @Query("Select * from user_table where :email = email and :passwordHash = passwordHash Limit 1")
    suspend fun getUser(email : String, passwordHash : String) : EntityUser?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertRestaurant(entityRestaurant: EntityRestaurant)

    @Query("Select * from restaurant_table where :city = city and :page = page")
    suspend fun getRestaurants(city: String, page: Int) : List<EntityRestaurant>

    @Query("Select COUNT(*) from restaurant_table where :city = city and :page = page")
    suspend fun getCount(city : String, page: Int): Int

    @Query("Delete from restaurant_table")
    suspend fun deleteRestaurants()

    @Query("Delete from user_table")
    suspend fun deleteUsers()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertCity(entityCity: EntityCity)

    @Query("Select name from city_table")
    suspend fun getCityNames() : List<String>

    @Query("Select id from user_table where email = :email LIMIT 1")
    suspend fun getUserId(email: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite : EntityFavorite)

    @Query("Delete from favorite_table where restaurantId = :restaurantId and ownerId = :userId")
    suspend fun deleteFavorite(userId: Int, restaurantId: Int)

    @Query("Select COUNT(*) from favorite_table where restaurantId = :restaurantId and ownerId = :userId")
    suspend fun isLiked(userId : Int, restaurantId : Int) : Int

    @Query("Select * from favorite_table where ownerId = :userId")
    suspend fun getFavorites(userId : Int) : MutableList<EntityFavorite>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProfileImage(image : EntityProfilePicture)

    @Query("Select COUNT(*) from profile_pictures where userId = :userId")
    suspend fun profileImageIsExists(userId : Int) : Int

    @Query("Delete from profile_pictures where userId = :userId")
    suspend fun deleteProfileImage(userId: Int)

    @Query("Select * from profile_pictures where userId = :userId LIMIT 1")
    suspend fun getProfileImage(userId : Int) : EntityProfilePicture

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRestaurantImage(image : EntityRestaurantPicture)

    @Query("SELECT COUNT(*) from restaurant_pictures where restaurantId = :restaurantId")
    suspend fun getRestaurantPictureCount(restaurantId : Int) : Int

    @Query("SELECT * from restaurant_pictures where restaurantId = :restaurantId LIMIT 1")
    suspend fun getOneRestaurantPicture(restaurantId: Int)  : EntityRestaurantPicture

    @Query("SELECT * from restaurant_pictures where restaurantId = :restaurantId")
    suspend fun getAllRestaurantPicture(restaurantId: Int) : MutableList<EntityRestaurantPicture>


}