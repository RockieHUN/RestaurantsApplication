package com.example.luigi.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.luigi.repository.UserRepository
import com.example.luigi.room.User
import com.example.luigi.room.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class UserViewModel (application: Application): AndroidViewModel (application){
    private val readAllData : LiveData<List<User>>
    private val repository : UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: User){
        viewModelScope.launch (Dispatchers.IO){
            repository.addUser(user)
        }
    }

    fun getUser(email : String, passwordHash : String): User?{
        return repository.getUser(email,passwordHash)
        }

    }
