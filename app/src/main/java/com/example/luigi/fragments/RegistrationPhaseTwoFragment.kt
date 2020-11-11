package com.example.luigi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.luigi.R
import com.example.luigi.databinding.FragmentRegistrationPhaseTwoBinding

class RegistrationPhaseTwoFragment : Fragment() {

    private lateinit var binding : FragmentRegistrationPhaseTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_registration_phase_two,container,false)
        return binding.root
    }


}