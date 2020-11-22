package com.example.luigi.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.luigi.MapsActivity
import com.example.luigi.R
import com.example.luigi.databinding.FragmentDetailBinding
import com.example.luigi.viewModels.MyDatabaseViewModel

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //open map
        binding.mapButton.setOnClickListener {
            val restaurant = myDatabaseViewModel.restaurants.value?.get(myDatabaseViewModel.position)
            val intent = Intent(context, MapsActivity::class.java).apply {
                putExtra("lat",restaurant?.lat)
                putExtra("long",restaurant?.lng)
                putExtra("name",restaurant?.name)
            }
            startActivity(intent)
        }

        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }

        myDatabaseViewModel.restaurants.observe(requireActivity(), Observer { list ->
            val restaurant = list[myDatabaseViewModel.position]

            val city = "${restaurant.country}, ${restaurant.city}"
            val price = "Price: $" + restaurant.price.toString()

            binding.restaurantName.text = restaurant.name
            binding.restaurantCity.text = city
            binding.restaurantAddress.text = restaurant.address
            binding.restaurantTelephone.text = restaurant.phone
            binding.restaurantPrice.text = price
        })
    }

}