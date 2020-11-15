package com.example.luigi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentLoginBinding
import com.example.luigi.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.math.BigInteger
import java.security.MessageDigest


class LoginFragment : Fragment() {

    private lateinit var binding :FragmentLoginBinding
    private lateinit var sharedPref : SharedPreferences
    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get userViewModel
        userViewModel = requireActivity().run {
            ViewModelProvider(this).get(UserViewModel::class.java)
        }

        //listener for login button
        binding.loginButton.setOnClickListener{
            val email=binding.editTextEmail.text.toString()
            val password= binding.editTextPassword.text.toString()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                login(email,password)
            }
        }
    }

    // ******************** PRIVATE FUNCTIONS **********************

    private fun login(email : String,password : String){

        val user = userViewModel.getUser(email,md5(password))

        //check if the user is exists in database
        //if exists save to sharedPref and navigate to the main menu
        if (user != null )
        {
            sharedPref= requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
            val edit= sharedPref.edit()
            edit.clear()
            edit.putString("email",email)
            edit.putString("password",md5(password))
            edit.apply()
            findNavController().navigate(R.id.action_loginFragment_to_mainMenuFragment)
        }
        else{
            binding.editTextEmail.error="Invalid password or email!"
        }
    }

    private fun md5(input : String): String {

        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}
