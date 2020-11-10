package com.example.luigi.repository

import com.example.luigi.room.User
import com.example.luigi.room.UserDao

class UserRepository(private val userDao : UserDao) {

    suspend fun addUser(user : User){
        userDao.addUser(user)
    }
}