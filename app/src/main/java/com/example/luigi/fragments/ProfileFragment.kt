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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentProfileBinding
import com.example.luigi.utils.ImageUtils
import com.example.luigi.viewModels.MyDatabaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
        myDatabaseViewModel.profileImage.observe(viewLifecycleOwner,  { image ->
            if (image != null){
                binding.profilePicture.setImageBitmap(image)
            }
        })
        myDatabaseViewModel.loadProfileImage()

        //On logout, clear shared preferences and navigate to the login fragment
        binding.logoutButton.setOnClickListener{
            logout()
        }

        //onBackPressed, navigate to the main menu
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_profileFragment_to_mainMenuFragment)
        }

        //upload image
        binding.editProfilePicture.setOnClickListener {
            pickImageFromGallery()
        }

    }

    //getting the activity result (picture)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null){
            val imageUri = data.data

            //set profile image
            myDatabaseViewModel.profileImage.observe(viewLifecycleOwner,{bitmap ->
                binding.profilePicture.setImageBitmap(bitmap)
            })

            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {

                //convert uri to bitmap and scale down
                val bitmap = ImageUtils.uriToScaledBitmap(requireActivity(), imageUri!!)
                myDatabaseViewModel.profileImage.postValue(bitmap)

                //convert bitmap to byteArray
                val byteArray = ImageUtils.bitmapToByteArray(bitmap!!)

                //save byteArray to the database
                myDatabaseViewModel.addProfileImage(byteArray!!)
            }
        }
    }

    //starting an activity to get a pickture
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    private fun logout(){
        sharedPref= requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        myDatabaseViewModel.user.value = null
        myDatabaseViewModel.profileImage.value = null

        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }
}