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
import com.example.luigi.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding :FragmentLoginBinding
    private lateinit var sharedPref : SharedPreferences

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

        //TODO: hints!
        binding.loginButton.setOnClickListener{
            val email=binding.editTextEmail.text.toString()
            val password= binding.editTextPassword.text.toString()
            login(email,password)
        }
    }

    private fun login(email:String,password:String){


                sharedPref= requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val edit= sharedPref.edit()
                edit.clear()
                edit.putString("email",email)
                edit.putString("password",password)
                edit.apply()
                findNavController().navigate(R.id.action_loginFragment_to_mainMenuFragment)

                binding.editTextEmail.error="Invalid password or email!"
        }
    }
