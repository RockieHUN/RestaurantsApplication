package com.example.luigi.room.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.luigi.room.entities.EntityCity
import com.example.luigi.room.entities.EntityRestaurant
import com.example.luigi.room.entities.EntityUser

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




}