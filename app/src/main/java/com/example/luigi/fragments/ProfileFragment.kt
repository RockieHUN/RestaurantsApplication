package com.example.luigi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    //TODO onBackPressed

    private lateinit var binding : FragmentProfileBinding
    private lateinit var sharedPref : SharedPreferences


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

        sharedPref= requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)

        binding.logoutButton.setOnClickListener{
            sharedPref.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

}