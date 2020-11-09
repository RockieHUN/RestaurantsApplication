package com.example.luigi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.luigi.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SplashScreenFragment : Fragment() {
    private lateinit var binding : FragmentSplashScreenBinding
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_splash_screen,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //try to login after 2 seconds
        Timer().schedule(object : TimerTask() {
            override fun run() {

                sharedPref = requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)
                val credentials = sharedPref.all
                login(credentials)

            }
        }, 2000)
    }

    //attempting to login. if it fails then navigate to register
    private fun login(preferences:MutableMap<String,*>){

        //check if credentials already exists
        if (preferences.containsKey("email") && preferences.containsKey("password")){

            //get firebase instance
            val auth = FirebaseAuth.getInstance()

            // try to sing in and if it is successfil navigate to the main screen
            auth.signInWithEmailAndPassword(preferences["email"].toString(),preferences["password"].toString()).addOnCompleteListener{task->
                if (task.isSuccessful){
                    findNavController().navigate(R.id.action_splashScreenFragment_to_mainMenuFragment)
                }

                //if login is not successful navigate to register
                else{
                    sharedPref.edit().clear().apply()
                    findNavController().navigate(R.id.action_splashScreenFragment_to_register2)
                }
            }
        }
        else{
            //if credentials does not exists successful navigate to register
            findNavController().navigate(R.id.action_splashScreenFragment_to_register2)
        }
    }

}