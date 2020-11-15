package com.example.luigi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.luigi.R
import com.example.luigi.databinding.FragmentRegisterBinding
import com.example.luigi.model.RegistrationUser
import com.example.luigi.model.Restaurant
import com.example.luigi.room.User
import com.example.luigi.viewModels.RegistrationViewModel
import com.example.luigi.viewModels.UserViewModel
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.concurrent.timerTask


class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private lateinit var sharedPref : SharedPreferences
    private val registrationViewModel : RegistrationViewModel by activityViewModels()
    private lateinit var userViewModel : UserViewModel

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

        // init viewModels
        val user = RegistrationUser("","","","","")
        registrationViewModel.setUser(user)

        userViewModel = requireActivity().run{
            ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        }


        //load the first phase of the registration
        var transaction = parentFragmentManager.beginTransaction()
        var fragment = RegistrationPhaseOneFragment()
        transaction.add(R.id.registration_container,fragment)
        transaction.commit()



        // the user can navigate from register screen to login screen
        binding.IAlreadyHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_loginFragment)
        }


        var currentFragment = 1       //counter what determinate the current fragment

        binding.registerButton.setOnClickListener {
            when (currentFragment) {
                //on the fragment 1 save validate and save the inputs then load the next fragmnet
                1 -> {
                    if (firstInformationIsValid()){
                        loadFragment(2)
                        currentFragment ++
                    }

                }

                //on fragment 2 validate second phase inputs and save user to database, then navigate to the main screen
                2 ->{
                    if (secondPhaseIsValid()){
                        saveSecondPhaseData()
                        register()
                        findNavController().navigate(R.id.action_register_to_mainMenuFragment)
                    }

                }
            }
        }


        // variable to count how many times the user tapped the back button in a given time (2 seconds)
        var callbackCounter = 0

        // onBackPressed listener
        requireActivity().onBackPressedDispatcher.addCallback(this) {

            if (currentFragment == 1){

                //on fragment 1 the user need to tap 2 times the back button under 2 seconds to exit from the application
                if (callbackCounter == 0) {
                    Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT).show()
                    Timer().schedule(timerTask {
                        callbackCounter = 0
                    }, 2000)

                    callbackCounter++
                }
                else requireActivity().finish()
            }
            //on the second fragment save inputs and load the first fragment on back button pressed
            else{
                saveSecondPhaseData()
                currentFragment = 1
                loadFragment(1)

            }



        }
    }

    // ********************* PRIVATE FUNCTIONS *****************

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


    //Check if inputs are valid and set viewModel values
    private fun firstInformationIsValid() : Boolean{

        //get inputs
        val emailView = binding.registrationContainer.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val pwView = binding.registrationContainer.findViewById<EditText>(R.id.editTextTextPassword)
        val pw2View = binding.registrationContainer.findViewById<EditText>(R.id.editTextTextPassword2)

        //validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailView.text.toString()).matches() || emailView.text.toString().count() < 5)
        {
            emailView.error = "Invalid email!"
            return false
        }

        val pwRegex ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$".toRegex()
        if (!pwRegex.matches(pwView.text.toString()) || pw2View.text.toString().count()<8){
            pwView.error = "Must contain at least 8 charachters with at least 1 uppercase letter and 1 number"
            return false
        }

        if (pwView.text.toString()!=pw2View.text.toString()){
            pw2View.error = "Passwords do not match!"
            return false
        }

        //set values for viewModel
        var user = registrationViewModel.getUser()
        user?.email=emailView.text.toString()
        user?.password=pw2View.text.toString()

        return true
    }

    private fun saveSecondPhaseData(){
        val nameView = binding.registrationContainer.findViewById<EditText>(R.id.editText_Name)
        val phoneView = binding.registrationContainer.findViewById<EditText>(R.id.editText_Phone)
        val addressView = binding.registrationContainer.findViewById<EditText>(R.id.editText_Address)

        var user = registrationViewModel.getUser()
        user?.name = nameView.text.toString()
        user?.phone_number = phoneView.text.toString()
        user?.address = addressView.text.toString()

    }

    private fun secondPhaseIsValid() :Boolean{
        val nameView = binding.registrationContainer.findViewById<EditText>(R.id.editText_Name)
        val phoneView = binding.registrationContainer.findViewById<EditText>(R.id.editText_Phone)
        val addressView = binding.registrationContainer.findViewById<EditText>(R.id.editText_Address)

        val phoneRegex = "^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}\$".toRegex()
        if (nameView.text.toString().count() < 4) {
            nameView.error ="Invalid name. 4 character needed at least"
            return false
        }

        if (addressView.text.toString().count()< 5) {
            addressView.error="Invalid address. 5 character needed at least"
            return false
        }

        if (phoneView.text.toString().count() < 10 || !phoneRegex.matches(phoneView.text.toString())) {
            phoneView.error = "Invalid phone number"
            return false
        }

        return true
    }



    private fun register(){
       var regUser = registrationViewModel.getUser()

        //create user for database
        val user = User(
                0,
                md5(regUser?.password!!),
                regUser.name,
                regUser.address,
                regUser.phone_number,
                regUser.email

        )

        //TODO: Check if user exists
        //adding user to database
        userViewModel.addUser(user)

        //saving credentials to sharedPref
        sharedPref = context?.getSharedPreferences("credentials", Context.MODE_PRIVATE)!!
        var editor=sharedPref.edit()
        editor.clear()
        editor.putString("email",regUser.email)
        editor.putString("password",md5(regUser.password))
        editor.apply()

        Toast.makeText(context,"Registration succesfull!",Toast.LENGTH_SHORT).show()

    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}