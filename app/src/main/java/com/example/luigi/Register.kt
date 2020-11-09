package com.example.luigi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.luigi.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Register : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // the user can navigate from register screen to login screen
        binding.IAlreadyHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_loginFragment)
        }

        //set onclick listener to registerButton
        binding.registerButton.setOnClickListener {

            //get values from text fields
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            val password_again = binding.editTextTextPassword2.text.toString()

            register(email, password, password_again )
        }
    }

    //TODO: Regex
    private fun register(email: String, password: String, password_again: String){
        var appContext = requireContext()

        //checking inputs
        if (email.isNullOrEmpty() || password.isNullOrEmpty() || password_again.isNullOrEmpty()) {
            Toast.makeText(appContext,"Please fill in every field!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length<6)
        {
            binding.editTextTextPassword.error = "Password is too short"
            return
        }

        if (password != password_again) {
            binding.editTextTextPassword2.error = "Passwords doesn't match!"
            return
        }


        sharedPref = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        var auth = FirebaseAuth.getInstance()

        // if the registration is successful save to sharedPef and navigate to the main screen
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //save to sharedPref
                    var editor=sharedPref.edit()
                    editor.clear()
                    editor.putString("email",email)
                    editor.putString("password",password)
                    editor.apply()
                    Toast.makeText(appContext, "Registration succesfull!", Toast.LENGTH_SHORT).show()

                    //navigate to main screen
                    findNavController().navigate(R.id.action_register_to_mainMenuFragment)

                } else {
                    Toast.makeText(appContext, "Bad credentials! Please try again!", Toast.LENGTH_SHORT).show()
                }
            }

    }
}