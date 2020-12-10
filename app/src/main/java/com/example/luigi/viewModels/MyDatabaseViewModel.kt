package com.example.luigi.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.luigi.model.*
import com.example.luigi.repository.MyDatabaseRepository
import com.example.luigi.room.MyDatabase
import com.example.luigi.room.entities.*
import com.example.luigi.utils.ClassConverter
import com.example.luigi.utils.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent

class MyDatabaseViewModel (application: Application): AndroidViewModel (application) {
    private val readAllData: LiveData<List<EntityUser>>
    private val repository: MyDatabaseRepository

    //the loaded data will be stored in these variables
    var restaurants : MutableLiveData<List<RestaurantWithPicture>> = MutableLiveData<List<RestaurantWithPicture>>()
    var restaurantProfiles : MutableLiveData <MutableList<RestaurantPicture>> = MutableLiveData<MutableList<RestaurantPicture>>()
    var cityNames : List<String> = listOf<String>()
    var currentCity : MutableLiveData<String> = MutableLiveData<String>()
    var favorites : MutableLiveData<MutableList<FavoriteWithPicture>> = MutableLiveData<MutableList<FavoriteWithPicture>>()

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

    //restaurantPictures
    var restaurantPictures : MutableLiveData<MutableList<RestaurantPicture>> = MutableLiveData<MutableList<RestaurantPicture>>()


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
            loadFavorites()
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
            val entities = repository.getRestaurants(city, page)
            val models = mutableListOf<RestaurantWithPicture>()
            for (entity in entities){

                var image : Bitmap? = null
                if (repository.getRestaurantPictureCount(entity.id)> 0)
                    image = ImageUtils.byteArrayToBitmap(repository.getOneRestaurantPicture(entity.id).image!!)

                val model = ClassConverter.restaurantToWithPicture(entity)
                model.image = image
                models.add(model)
            }
            restaurants.value = models
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
    fun like (restaurant : RestaurantWithPicture){
        viewModelScope.launch {
            val userId = repository.getUserId(user.value!!.email)

            //convert to entity
            val entityFavorite = ClassConverter.restaurantWithPictureToEntityFavorite(restaurant, userId)


            if (repository.isLiked(userId,entityFavorite.restaurantId)){
                //delete from database
                repository.deleteFavorite(entityFavorite.ownerId, entityFavorite.restaurantId)

                //delete from the viewModel list
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

                //get image
                var image : Bitmap? = null
                if (repository.getRestaurantPictureCount(entityFavorite.restaurantId)> 0)
                    image = ImageUtils.byteArrayToBitmap(repository.getOneRestaurantPicture(entityFavorite.restaurantId).image!!)

                //convert entity
                val withPicture = ClassConverter.favoriteToWithPicture(entityFavorite)
                withPicture.image = image

                favorites.value?.add(withPicture)
            }


        }
    }

    fun deleteFavorite(entityFavorite: EntityFavorite){
        viewModelScope.launch {
            repository.deleteFavorite(entityFavorite.ownerId, entityFavorite.restaurantId)
            if (favorites.value != null){

                //remove the favorite from the copy of the original list
                val tempList = favorites.value
                tempList!!.removeIf{favorite ->
                    favorite.ownerId == entityFavorite.ownerId && favorite.restaurantId == entityFavorite.restaurantId
                }

                //update the original list
                favorites.value = tempList
            }
        }
    }

    //load the favorite restaurants of a user
    fun loadFavorites(){
        viewModelScope.launch {
            val userId = repository.getUserId(user.value!!.email)
            val result = repository.getFavorites(userId)

            //convert entities
            if (result != null){
                val list : MutableList<FavoriteWithPicture> = mutableListOf()

                for (entity in result){
                    val favorite = ClassConverter.favoriteToWithPicture(entity)

                    var image : Bitmap? = null
                    if (repository.getRestaurantPictureCount(entity.restaurantId)> 0)
                        image = ImageUtils.byteArrayToBitmap(repository.getOneRestaurantPicture(entity.restaurantId).image!!)
                    favorite.image = image
                    list.add(favorite)
                }

                favorites.value = list
            }


        }

    }

    //insert a profile image to the database as ByteArray
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

    /*
    get the profile picture of the user from database as ByteArray
    then convert it to Bitmap
     */
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

    //add an image to a restaurant
    fun addRestaurantImage(image : ByteArray, restaurantId : Int){
        viewModelScope.launch (Dispatchers.IO){
            val userId= repository.getUserId(user.value!!.email)

            val entityRestaurantPicture = EntityRestaurantPicture(
                0,
                restaurantId,
                userId,
                image
            )
            //add to database
            repository.addRestaurantPicture(entityRestaurantPicture)

        }
    }

    //get the userId of a user with email
    suspend fun getUserId(email : String) : Int{
        return repository.getUserId(user.value!!.email)
    }

    //load every picture of a restaurant
    fun loadRestaurantPictures(restaurantId: Int){
        viewModelScope.launch (Dispatchers.IO){
            val count = repository.getRestaurantPictureCount(restaurantId)
            if (count > 0){
                //get byteRestaurants
                val response =repository.getAllRestaurantPicture(restaurantId)
                val list : MutableList<RestaurantPicture> = mutableListOf()

                //convert byteRestaurants to bitmapRestaurants
                for (byteRestaurant in response){
                    val newItem = RestaurantPicture(
                        byteRestaurant.restaurantPictureId,
                        byteRestaurant.restaurantId,
                        byteRestaurant.userId,
                        ImageUtils.byteArrayToBitmap(byteRestaurant.image!!)
                    )
                    list.add(newItem)
                }
                restaurantPictures.postValue(list)
            }
            else{
                restaurantPictures.postValue(mutableListOf())
            }
        }
    }

    fun loadRestaurantProfiles(){
        viewModelScope.launch {
            if (restaurants.value != null){

                //init temp list
                val list : MutableList<RestaurantPicture> = mutableListOf()

                //iterate through the restaurants
                for ( restaurant in restaurants.value!!){

                    //if the restaurant has at least one picture get it and convert it to bitmap
                    if (repository.getRestaurantPictureCount(restaurant.id)> 0){

                        val byteArrayClass = repository.getOneRestaurantPicture(restaurant.id)
                        val bitmap = ImageUtils.byteArrayToBitmap(byteArrayClass.image!!)
                        val bitmapClass = RestaurantPicture(
                            byteArrayClass.restaurantPictureId,
                            restaurant.id,
                            byteArrayClass.userId,
                            bitmap)

                        list.add(bitmapClass)
                    }
                    else{
                        // do nothing
                    }
                }
                restaurantProfiles.value = list
            }
            else{
                //do nothing
            }

        }
    }
}