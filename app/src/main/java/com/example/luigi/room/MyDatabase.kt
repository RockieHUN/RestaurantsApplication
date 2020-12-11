package com.example.luigi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.luigi.room.daos.MyDatabaseDao
import com.example.luigi.room.entities.*

@Database(entities = [
    EntityUser::class,
    EntityRestaurant::class,
    EntityCity::class,
    EntityFavorite::class,
    EntityProfilePicture::class,
    EntityRestaurantPicture::class
                     ],
    version = 12,
    exportSchema = false)
abstract class MyDatabase : RoomDatabase(){

    abstract fun userDao() : MyDatabaseDao


    companion object{
        @Volatile
        private var INSTANCE : MyDatabase? = null    //singleton, only one instance

        //get database
        fun getDatabase(context : Context): MyDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            //if database doesnt exists, create database
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}