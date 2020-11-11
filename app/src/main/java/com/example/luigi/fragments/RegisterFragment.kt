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
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentRegisterBinding
import com.example.luigi.room.User
import java.util.*
import kotlin.concurrent.timerTask


class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //init user
        val user = User(0,"","","","","", listOf())


        //load the first phase of the registration
        var transaction = parentFragmentManager.beginTransaction()
        var fragment = RegistrationPhaseOneFragment()
        transaction.add(R.id.registration_container,fragment)
        transaction.commit()



        // the user can navigate from register screen to login screen
        binding.IAlreadyHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_loginFragment)
        }


        var currentFragment = 1       //counter what determinate which fragments should be loaded

        binding.registerButton.setOnClickListener {
            when (currentFragment) {
                1 -> {
                    loadFragment(2)
                    currentFragment ++
                }
                //TODO: Registration
                2 ->{
                    findNavController().navigate(R.id.action_register_to_mainMenuFragment)
                }
            }
        }

        var callbackCounter = 0
        requireActivity().onBackPressedDispatcher.addCallback(this) {

            if (currentFragment == 1){
                if (callbackCounter == 0) {
                    Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT).show()
                    Timer().schedule(timerTask {
                        callbackCounter = 0
                    }, 2000)

                    callbackCounter++
                }
                else requireActivity().finish()
            }
            else{
                loadFragment(1)
                currentFragment = 1
            }



        }
    }

    private fun loadFragment(which : Int){
        val transaction : FragmentTransaction

        if (which == 2){
            transaction = parentFragmentManager.beginTransaction()
            var fragment = RegistrationPhaseTwoFragment()
            transaction.add(R.id.registration_container,fragment)
            transaction.commit()
            binding.registerButton.text="Register"
        }
        else{
            transaction = parentFragmentManager.beginTransaction()
            var fragment = RegistrationPhaseOneFragment()
            transaction.add(R.id.registration_container,fragment)
            transaction.commit()
            binding.registerButton.text="Next"
        }
    }

    //TODO: Regex
    private fun register(email: String, password: String, password_again: String){
        var appContext = requireContext()



        sharedPref = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!





    }
}