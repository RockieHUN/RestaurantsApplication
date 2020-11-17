package com.example.luigi.repository

import androidx.lifecycle.LiveData
import com.example.luigi.model.CityRestaurants
import com.example.luigi.room.entities.EntityUser
import com.example.luigi.room.daos.MyDatabaseDao
import com.example.luigi.room.entities.EntityRestaurant
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MyDatabaseRepository(private val myDatabaseDao : MyDatabaseDao) {

    val readAllData : LiveData<List<EntityUser>> = myDatabaseDao.readAllData()

    suspend fun addUser(entityUser : EntityUser){
        myDatabaseDao.addUser(entityUser)
    }

    fun getUser(email : String, passwordHash: String) : EntityUser?{
        return myDatabaseDao.getUser(email,passwordHash)
    }

    /*
    Converting restaurants returned by api (CityRestaurants) to EntityRestaurant
    what will be added to the database with a TIMESTAMP using suspend function
     */
    suspend fun addRestaurants(restaurants : List<EntityRestaurant>){
        for (i in 0 until restaurants.size){

            //get timestamp
            val timestamp =  DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now())

            restaurants[i].timestamp = timestamp

            myDatabaseDao.InsertRestaurant(restaurants[i])
        }
    }


    /*
    Get the count of restaurants of a given city and page,
    available in the database
     */
    suspend fun getCount(city : String, page: Int) : Int{
        return myDatabaseDao.getCount(city, page)
    }

    suspend fun getRestaurants(city : String, page: Int) : List<EntityRestaurant>{
        return myDatabaseDao.getRestaurants(city, page)
    }

}