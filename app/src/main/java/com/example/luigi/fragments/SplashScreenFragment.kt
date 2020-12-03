package com.example.luigi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentSplashScreenBinding
import com.example.luigi.repository.ApiRepository
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.ApiViewModelFactory
import com.example.luigi.viewModels.MyDatabaseViewModel
import java.util.*

class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel
    private lateinit var apiViewModel: ApiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get databaseViewModel
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }

        preLoadData()


        /*
        get credentials from database and try to log in
        if login is successful, load the data
         */
        sharedPref = requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val credentials = sharedPref.all
        login(credentials)

    }

    // ***************************** PRIVATE FUNCTIONS ***********************

    //attempting to login. if it fails then navigate to register
    private fun login(preferences: MutableMap<String, *>) {


        //check if credentials already exists
        if (preferences.containsKey("email") && preferences.containsKey("password")) {
            myDatabaseViewModel.user.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { user ->

                    //check if user exists in the database
                    if (user != null) {
                        //Log.d("**********************","observer SET")

                        //Navigate to the main menu fragment ONLY when the data is loaded
                        myDatabaseViewModel.loadedComponents.observe(
                            viewLifecycleOwner,
                            androidx.lifecycle.Observer { loadedComponents ->
                                if (loadedComponents >= 2) {
                                    myDatabaseViewModel.loadFavorites()
                                    findNavController().navigate(R.id.action_splashScreenFragment_to_mainMenuFragment)
                                }
                            })
                    } else {
                        sharedPref.edit().clear().apply()
                        findNavController().navigate(R.id.action_splashScreenFragment_to_register2)
                    }
                })

            myDatabaseViewModel.getUser(
                preferences["email"] as String,
                preferences["password"] as String
            )


        } else {
            sharedPref.edit().clear().apply()
            findNavController().navigate(R.id.action_splashScreenFragment_to_register2)
        }
    }

    private fun preLoadData() {
        val repository = ApiRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        apiViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ApiViewModel::class.java)


        /*
        preload the data.
        If the data is already in the database, load it
        else make and api request and save it to the database
         */

        myDatabaseViewModel.useApi.observe(
            requireActivity(),
            androidx.lifecycle.Observer { useApi ->
                if (useApi == true) {
                    Log.d("*********", "use API")


                    //Observer for restaurants
                    apiViewModel.restaurants.observe(
                        requireActivity(),
                        androidx.lifecycle.Observer { restaurants ->
                            //save the restaurants to the database
                            myDatabaseViewModel.InsertRestaurants(restaurants)
                            myDatabaseViewModel.restaurants.value = restaurants
                            myDatabaseViewModel.componentLoaded()
                        })

                    //Observer for city names
                    apiViewModel.cityNames.observe(
                        requireActivity(),
                        androidx.lifecycle.Observer { cities ->
                            myDatabaseViewModel.insertCities(cities)
                            myDatabaseViewModel.cityNames = cities
                            myDatabaseViewModel.componentLoaded()
                        })

                    //get restaurants and city names with API
                    apiViewModel.LoadDataWithAPI()

                } else {
                    // DO NOTHING
                }
            })

        myDatabaseViewModel.useDatabase.observe(
            requireActivity(),
            androidx.lifecycle.Observer { useDatabase ->
                if (useDatabase == true) {
                    Log.d("*********", "use DATABASE")

                    //load restaurants from the database
                    myDatabaseViewModel.loadRestaurantsFromDatabase("New York", 1)
                    myDatabaseViewModel.loadCityNamesFromDatabase()


                } else {
                    // DO NOTHING
                }
            })

        myDatabaseViewModel.getCount("New York", 1)
    }


}

