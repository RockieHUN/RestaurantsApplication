package com.example.luigi.repository

import androidx.lifecycle.LiveData
import com.example.luigi.room.User
import com.example.luigi.room.UserDao

class UserRepository(private val userDao : UserDao) {

    val readAllData : LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user : User){
        userDao.addUser(user)
    }

    fun getUser(email : String, passwordHash: String) : User?{
        return userDao.getUser(email,passwordHash)
    }
}