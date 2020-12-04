package com.example.luigi.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.luigi.R
import com.example.luigi.databinding.FragmentProfileBinding
import com.example.luigi.utils.ImageUtils
import com.example.luigi.viewModels.MyDatabaseViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var sharedPref : SharedPreferences
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //show user details
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }
        val user = myDatabaseViewModel.user.value
        binding.profileName.text = user?.name
        binding.profileEmail.text = user?.email
        binding.profileAddress.text = user?.address
        binding.profilePhone.text = user?.phone_number

        //load profile image
        myDatabaseViewModel.profileImage.observe(viewLifecycleOwner,  { entityProfilePicture ->
            if (entityProfilePicture != null){
                val bitmap = ImageUtils.toBitmap(entityProfilePicture.image!!)
                binding.profilePicture.setImageBitmap(bitmap)
            }
        })
        myDatabaseViewModel.loadProfileImage()

        //On logout, clear shared preferences and navigate to the login fragment
        binding.logoutButton.setOnClickListener{
            sharedPref= requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        //onBackPressed, navigate to the main menu
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_profileFragment_to_mainMenuFragment)
        }

        //upload image
        binding.editProfilePicture.setOnClickListener {
            pickImageFromGalery()
        }

    }

    //getting the activity result (picture)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null){
            val imageUri = data.data

            //set profile image
            binding.profilePicture.setImageURI(imageUri)

            //convert image to byteArray with custom library
            val byteArray = ImageUtils.toByteArray(requireActivity(),imageUri!!)

            //save byteArray to the database
            myDatabaseViewModel.addProfileImage(byteArray!!)
        }
    }

    //starting an activity to get a pickture
    private fun pickImageFromGalery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }
}