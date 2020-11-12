package com.example.luigi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.luigi.R
import com.example.luigi.databinding.FragmentRegistrationPhaseTwoBinding
import com.example.luigi.viewModels.RegistrationViewModel

class RegistrationPhaseTwoFragment : Fragment() {

    private lateinit var binding : FragmentRegistrationPhaseTwoBinding
    private val viewModel : RegistrationViewModel by activityViewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var user = viewModel.getUser()
        if (user != null){
            binding.editTextAddress.setText(user.address)
            binding.editTextName.setText(user.name)
            binding.editTextPhone.setText(user.phone_number)
        }
    }

}