package com.example.luigi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentProfileBinding
import com.example.luigi.viewModels.MyDatabaseViewModel
import java.util.*
import kotlin.concurrent.timerTask


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var sharedPref : SharedPreferences
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
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
    }

}