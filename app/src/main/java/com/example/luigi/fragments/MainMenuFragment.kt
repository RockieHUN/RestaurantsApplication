package com.example.luigi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.adapters.MainDataAdapter
import com.example.luigi.R
import com.example.luigi.databinding.FragmentMainMenuBinding
import com.example.luigi.repository.ApiRepository
import com.example.luigi.utils.ClassConverter
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.ApiViewModelFactory
import com.example.luigi.viewModels.MyDatabaseViewModel
import kotlinx.android.synthetic.main.fragment_main_menu.view.*
import java.util.*
import kotlin.concurrent.timerTask


class MainMenuFragment : Fragment(), MainDataAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var apiViewModel: ApiViewModel
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //DATABASE VIEWMODEL
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }



        //reset picture list
        myDatabaseViewModel.restaurantPictures.value = mutableListOf()

        binding.selectButton.text ="New York"

        //Add listener to the city selection button
        binding.appBarLayout.constraint_toolbarHolder.select_button.setOnClickListener{
            val dialog = MyDialogFragment()
            dialog.show(requireActivity().supportFragmentManager,"costumDialog")
        }

        //show bottom navigation menu
        requireActivity().findViewById<View>(R.id.bottom_navigaton).visibility = View.VISIBLE

        //API VIEWMODEL
        val repository = ApiRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        apiViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ApiViewModel::class.java)

        //remove observers set on previous fragments
        myDatabaseViewModel.removeObservers(requireActivity())


        /*
        When back button is pressed, start timer.
        If the user presses the button again under 2 seconds, exit from the application
        else reset the callbackCounter
         */
        var callbackCounter = 0

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (callbackCounter == 0) {
                Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT).show()
                Timer().schedule(timerTask {
                    callbackCounter = 0
                }, 2000)

                callbackCounter++
            } else requireActivity().finish()
        }

        var dialog : DialogFragment? = null


        var adapter : MainDataAdapter? = null

        //OBSERVING THE DATA AND PASSING TO THE RECYCLE VIEW
        myDatabaseViewModel.restaurants.observe(requireActivity(), { restaurants ->
            adapter = MainDataAdapter(restaurants, this,myDatabaseViewModel)
            binding.recycleView.adapter = adapter
            if (dialog != null){
                dialog?.dismiss()
            }
            else{
                //do nothing
            }
        })
        binding.recycleView.layoutManager = LinearLayoutManager(context)
        binding.recycleView.setHasFixedSize(true)

        //OBSERVING THE CURRENT CITY AND LOADING THE CITY's RESTAURANTS
        myDatabaseViewModel.currentCity.observe(viewLifecycleOwner, androidx.lifecycle.Observer { city ->
            dialog = LoadingFragment()
            dialog?.show(requireActivity().supportFragmentManager,"loading")
            binding.selectButton.text = city
            loadRestaurants(city)
        })

        //SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (adapter != null) {
                    adapter!!.filter.filter(newText)
                    binding.recycleView.recycledViewPool.clear()
                    adapter!!.notifyDataSetChanged()
                }
                return true
            }
        })


    }

    override fun onItemClick(position: Int, restaurantName: String) {
       myDatabaseViewModel.restaurantName = restaurantName
        findNavController().navigate(R.id.action_mainMenuFragment_to_detailFragment)
    }

    private fun loadRestaurants(city : String){

        //reset variables
        myDatabaseViewModel.useApi.removeObservers(viewLifecycleOwner)
        myDatabaseViewModel.useApi.removeObservers(viewLifecycleOwner)
        myDatabaseViewModel.useApi.value = false
        myDatabaseViewModel.useDatabase.value = false



        //observe useApi variable
        myDatabaseViewModel.useApi.observe(viewLifecycleOwner,  { useApi ->
            if (useApi){
                apiViewModel.getCityRestaurants(city)
                apiViewModel.restaurants.observe(viewLifecycleOwner,  { restaurants ->
                    myDatabaseViewModel.restaurants.value = ClassConverter.listRestaurantToWithPicture(restaurants)
                    myDatabaseViewModel.InsertRestaurants(restaurants)
                })
            }
            else{
                //DO NOTHING
            }
        })

        //observe useDatabase variable
        myDatabaseViewModel.useDatabase.observe(viewLifecycleOwner,  {  useDatabase ->
            if (useDatabase){
                myDatabaseViewModel.loadRestaurantsFromDatabase(city, 1)
            }
            else{
                // DO NOTHING
            }
        })

        myDatabaseViewModel.getCount(city,1)
    }


}