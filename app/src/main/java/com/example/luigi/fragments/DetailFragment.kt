package com.example.luigi.fragments

import android.app.Activity
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
import com.example.luigi.room.entities.EntityRestaurant
import com.example.luigi.utils.ImageUtils
import com.example.luigi.viewModels.MyDatabaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        //database viewModel
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }

        val restaurant = getRestaurant()

        //load restaurant images
        myDatabaseViewModel.restaurantPictures.observe(viewLifecycleOwner,{pictureList ->
            if (pictureList.size > 0){
                binding.restaurantProfilePicture.setImageBitmap(pictureList[0].image)
                if (pictureList.size > 1){
                    //TODO: add images to adapter
                }
                else{
                    //do nothing
                }
            }
            else{
                // do nothing
            }
        })
        myDatabaseViewModel.loadRestaurantPictures(restaurant.id)


        //show restaurant on map
        binding.mapButton.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java).apply {
                putExtra("lat",restaurant.lat)
                putExtra("long",restaurant.lng)
                putExtra("name",restaurant.name)
            }
            startActivity(intent)
        }


        //show every information of a restaurant on the UI
        myDatabaseViewModel.restaurants.observe(requireActivity(), Observer { list ->
            val city = "${restaurant.country}, ${restaurant.city}"
            val price = "Price: $" + restaurant.price.toString()

            binding.restaurantName.text = restaurant.name
            binding.restaurantCity.text = city
            binding.restaurantAddress.text = restaurant.address
            binding.restaurantTelephone.text = restaurant.phone
            binding.restaurantPrice.text = price
        })

        //adding an image to the restaurant
        binding.addImageButton.setOnClickListener {
            pickImageFromGallery()
        }

    }

    private fun getRestaurant() : EntityRestaurant {
        return myDatabaseViewModel.restaurants.value!!.find { restaurant ->
            restaurant.name == myDatabaseViewModel.restaurantName
        }!!
    }

    //starting an activity to get a pickture
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    //getting the activity result (picture)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            val imageUri = data.data


            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {

                //convert uri to bitmap and scale down
                val bitmap = ImageUtils.uriToScaledBitmap(requireActivity(), imageUri!!)
                //myDatabaseViewModel.profileImage.postValue(bitmap)

                //convert bitmap to byteArray
                val byteArray = ImageUtils.bitmapToByteArray(bitmap!!)

                //save byteArray to the database
                myDatabaseViewModel.addRestaurantImage(byteArray!!, getRestaurant().id)
            }
        }
    }

}