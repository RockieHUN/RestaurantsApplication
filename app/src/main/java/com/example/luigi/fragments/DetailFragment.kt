package com.example.luigi.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.MapsActivity
import com.example.luigi.R
import com.example.luigi.adapters.ImageAdapter
import com.example.luigi.databinding.FragmentDetailBinding
import com.example.luigi.model.RestaurantPicture
import com.example.luigi.model.RestaurantWithPicture
import com.example.luigi.utils.ImageUtils
import com.example.luigi.viewModels.MyDatabaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailFragment : Fragment(), ImageAdapter.OnItemClickListener {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel
    private lateinit var restaurant : RestaurantWithPicture

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

        //reset picture list
        myDatabaseViewModel.restaurantPictures.value = mutableListOf()

        restaurant = getRestaurant()

        //load restaurant images
        myDatabaseViewModel.restaurantPictures.observe(viewLifecycleOwner, { pictureList ->
            if (pictureList.size > 0) {
                binding.restaurantProfilePicture.setImageBitmap(pictureList[0].image)

                val adapter = ImageAdapter(pictureList, this, myDatabaseViewModel)
                binding.recycleView.adapter = adapter

            } else {
                // do nothing
            }
        })
        val horizontalLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycleView.layoutManager = horizontalLayout
        binding.recycleView.setHasFixedSize(true)

        myDatabaseViewModel.loadRestaurantPictures(restaurant.id)


        //show restaurant on map
        binding.mapButton.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java).apply {
                putExtra("lat", restaurant.lat)
                putExtra("long", restaurant.lng)
                putExtra("name", restaurant.name)
            }
            startActivity(intent)
        }

        //call activity
        binding.callButton.setOnClickListener {
            call(restaurant.phone)
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

    private fun getRestaurant() : RestaurantWithPicture {
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

                //refresh list
                val list = myDatabaseViewModel.restaurantPictures.value
                val userId = myDatabaseViewModel.getUserId(myDatabaseViewModel.user.value!!.email)
                val pictureClass = RestaurantPicture(0, restaurant.id, userId, bitmap)
                list!!.add(pictureClass)
                myDatabaseViewModel.restaurantPictures.postValue(list)
            }
        }
    }

    override fun onItemClick(position: Int) {
        return
    }

    //starting call activity
    private fun call(phoneNumber: String){
        val number = "tel:$phoneNumber"
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse(number)

        //check call permission
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {

                //if call permission is not granted, request permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
        }
        else {
            try {
                startActivity(intent)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

}