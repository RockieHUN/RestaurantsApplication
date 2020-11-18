package com.example.luigi

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.luigi.databinding.ActivityMainBinding
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.MyDatabaseViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        supportActionBar?.hide()

        //set up bottom navigation menu
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigaton.setupWithNavController(navController)
        binding.bottomNavigaton.visibility = View.GONE
    }
}