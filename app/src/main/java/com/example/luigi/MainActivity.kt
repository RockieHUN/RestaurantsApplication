package com.example.luigi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.luigi.databinding.ActivityMainBinding
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.MyDatabaseViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : ApiViewModel


    private lateinit var myDatabaseViewModel : MyDatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        supportActionBar?.hide()

        /*val repository = ApiRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ApiViewModel::class.java)*/

    }
}