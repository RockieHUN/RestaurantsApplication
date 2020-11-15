package com.example.luigi.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.luigi.room.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user : User)

    @Query("Select * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("Select * from user_table where :email = email and :passwordHash = passwordHash Limit 1")
    fun getUser(email : String, passwordHash : String) : User?
}