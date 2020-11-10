package com.example.luigi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1,exportSchema = false)
abstract class UserDatabase : RoomDatabase(){

    abstract fun userDao() : UserDao


    companion object{
        //singleton, only one instance
        @Volatile
        private var INSTANCE : UserDatabase? = null

        //get database
        fun getDatabase(context : Context): UserDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            //if database doesnt exists, create database
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}