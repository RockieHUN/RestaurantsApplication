package com.example.luigi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.luigi.databinding.ActivityMainBinding
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding


    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        supportActionBar?.hide()

    }
}