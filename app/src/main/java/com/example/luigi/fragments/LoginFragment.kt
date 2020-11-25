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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentLoginBinding
import com.example.luigi.viewModels.MyDatabaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.concurrent.timerTask


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //hide bottom nav menu
        requireActivity().findViewById<View>(R.id.bottom_navigaton).visibility = View.GONE

        //on back button pressed, navigate to the register fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_loginFragment_to_register)
        }

        //get userViewModel
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(this).get(MyDatabaseViewModel::class.java)
        }

        //listener for login button
        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            val scope = CoroutineScope(Dispatchers.IO)

            login(email, password)

        }
    }

    // ******************** PRIVATE FUNCTIONS **********************

    private fun login(email: String, password: String) {

        //check if the user is exists in database
        //if exists save to sharedPref and navigate to the main menu
        myDatabaseViewModel.user.observe(viewLifecycleOwner, androidx.lifecycle.Observer { user ->

            if (user != null) {
                sharedPref = requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val edit = sharedPref.edit()
                edit.clear()
                edit.putString("email", email)
                edit.putString("password", md5(password))
                edit.apply()
                findNavController().navigate(R.id.action_loginFragment_to_mainMenuFragment)
            } else {
                //TODO: THIS CANT BE ON BACKGROUND THREAD
                binding.editTextEmail.error = "Invalid password or email!"
            }
        })

       myDatabaseViewModel.getUser(email, md5(password))
    }

    private fun md5(input: String): String {

        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}
