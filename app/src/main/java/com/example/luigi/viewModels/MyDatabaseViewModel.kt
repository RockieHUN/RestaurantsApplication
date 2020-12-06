package com.example.luigi.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.luigi.model.CityRestaurants
import com.example.luigi.model.Restaurant
import com.example.luigi.repository.MyDatabaseRepository
import com.example.luigi.room.MyDatabase
import com.example.luigi.room.entities.*
import com.example.luigi.utils.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent

class MyDatabaseViewModel (application: Application): AndroidViewModel (application) {
    private val readAllData: LiveData<List<EntityUser>>
    private val repository: MyDatabaseRepository

    //the loaded data will be stored in these variables
    var restaurants : MutableLiveData<List<EntityRestaurant>> = MutableLiveData<List<EntityRestaurant>>()
    var cityNames : List<String> = listOf<String>()
    var currentCity : MutableLiveData<String> = MutableLiveData<String>()
    var favorites : MutableLiveData<MutableList<EntityFavorite>> = MutableLiveData<MutableList<EntityFavorite>>()

    //user
    var user : MutableLiveData<EntityUser> = MutableLiveData()
    var profileImage : MutableLiveData <Bitmap> = MutableLiveData<Bitmap>()

    //used for determining from where the data should be loaded (From internet or database)
    var useDatabase : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var useApi : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var loadedComponents : MutableLiveData<Int> = MutableLiveData<Int>()

    //used for communication between recycleView and Detail Fragment
    var restaurantName : String = "Orsay"
    var position : Int = 0


    //INIT
    init {
        loadedComponents.value = 0
        val userDao = MyDatabase.getDatabase(application).userDao()
        repository = MyDatabaseRepository(userDao)
        readAllData = repository.readAllData
    }


    //************ METHODS ************

    fun componentLoaded(){
        synchronized(this){
            loadedComponents.value = loadedComponents.value!! + 1
            //Log.d("****************componentLoaded + 1",loadedComponents.value.toString())
        }
    }

    // register the user in the database
    fun addUser(entityUser: EntityUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(entityUser)
        }
    }

    // get user from database (used at the login screen)
    fun getUser(email: String, passwordHash: String){
        viewModelScope.launch {
            val response =  repository.getUser(email, passwordHash)
            user.value = response
        }
    }

    // insert a list of restaurant to database
    fun InsertRestaurants(restaurants: List<EntityRestaurant>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRestaurants(restaurants)
        }
    }

    /*
    Check if the restaurants with the same city name and page number
    exist in the database. For this we will return the count.
     */
    fun getCount(city: String, page: Int){
        viewModelScope.launch {
            val count = repository.getCount(city, page)
            if (count > 0 ){
                useDatabase.value = true
            }
            else{
                useApi.value = true
            }
        }
    }

    /*
    Load restaurants of the given city and page
     */
    fun loadRestaurantsFromDatabase(city: String, page: Int){
        viewModelScope.launch {
            restaurants.value = repository.getRestaurants(city, page)
            componentLoaded()
        }
    }

    /*
     Delete every downloaded restaurants and
     data added by the user
     */
    fun deleteCache() {
        viewModelScope.launch {
            repository.deleteCache()
        }
    }

    //insert a list of cities to the database
    fun insertCities(cities : List<String>){
        viewModelScope.launch {
            repository.addCities(cities)
        }
    }

    //get the list of city names
    fun loadCityNamesFromDatabase(){
        viewModelScope.launch {
            cityNames = repository.getCityNames()
            componentLoaded()
        }
    }

    //remove all observers from every variable
    fun removeObservers(activity : FragmentActivity){
        viewModelScope.launch {
            restaurants.removeObservers(activity)
            user.removeObservers(activity)
            useApi.removeObservers(activity)
            useDatabase.removeObservers(activity)
            loadedComponents.removeObservers(activity)
        }
    }

    /*
    this method is implementing the like (adding or removing) functionality
    of a restaurant by determining the userId and checking if the user
    already liked a restaurant or not
     */
    fun like (restaurant : EntityRestaurant){
        viewModelScope.launch {
            val userId = repository.getUserId(user.value!!.email)

            val entityFavorite = EntityFavorite(
                0,
                userId,
                restaurant.id,
                restaurant.name,
                restaurant.address,
                restaurant.city,
                restaurant.state,
                restaurant.area,
                restaurant.postal_code,
                restaurant.country,
                restaurant.phone,
                restaurant.lat,
                restaurant.lng,
                restaurant.price,
                restaurant.mobile_reserve_url,
                restaurant.mobile_reserve_url,
                restaurant.image_url
            )
            if (repository.isLiked(userId,entityFavorite.restaurantId)){
                repository.deleteFavorite(entityFavorite.ownerId, entityFavorite.restaurantId)
                if (favorites.value != null) {
                    if (favorites.value!!.size == 1){
                        favorites.value = mutableListOf()
                    }
                    else{
                        favorites.value!!.remove(entityFavorite)
                    }

                }
                //favorites.value?.remove(entityFavorite)
            }
            else{
                repository.addFavorite(entityFavorite)
                favorites.value?.add(entityFavorite)
            }


        }
    }

    fun deleteFavorite(entityFavorite: EntityFavorite){
        viewModelScope.launch {
            repository.deleteFavorite(entityFavorite.ownerId, entityFavorite.restaurantId)
            if (favorites.value != null)  favorites.value!!.remove(entityFavorite)
        }
    }

    //load the favorite restaurants of a user
    fun loadFavorites(){
        viewModelScope.launch {
            val userId = repository.getUserId(user.value!!.email)
            val result = repository.getFavorites(userId)
            favorites.value = result
        }

    }

    fun addProfileImage(image : ByteArray){
        viewModelScope.launch(Dispatchers.IO){
            val userId = repository.getUserId(user.value!!.email)

            val entityProfilePicture = EntityProfilePicture(
                0,
                userId,
                image
            )
            repository.deleteProfileImage(userId)
            repository.addProfileImage(entityProfilePicture)
        }
    }

    fun loadProfileImage(){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = repository.getUserId(user.value!!.email)
            if (repository.profileImageIsExists(userId)){
                val byteArray = repository.getProfileImage(userId).image
                val bitmap = ImageUtils.byteArrayToBitmap(byteArray!!)
                profileImage.postValue(bitmap)
            }
        }
    }
}