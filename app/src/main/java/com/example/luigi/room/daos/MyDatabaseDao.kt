package com.example.luigi.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.luigi.room.entities.EntityRestaurant
import com.example.luigi.room.entities.EntityUser

@Dao
interface MyDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(entityUser : EntityUser)

    @Query("Select * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<EntityUser>>

    @Query("Select * from user_table where :email = email and :passwordHash = passwordHash Limit 1")
    fun getUser(email : String, passwordHash : String) : EntityUser?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertRestaurant(entityRestaurant: EntityRestaurant)
}