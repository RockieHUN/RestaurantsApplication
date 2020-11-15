package com.example.luigi.fragments

import android.content.ClipData
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.DataAdapter
import com.example.luigi.R
import com.example.luigi.databinding.FragmentMainMenuBinding
import com.example.luigi.model.Restaurant
import com.example.luigi.repository.ApiRepository
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.ApiViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainMenuFragment : Fragment() {

    private lateinit var binding : FragmentMainMenuBinding
    private lateinit var viewModel : ApiViewModel
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref= requireContext().getSharedPreferences("credentials", Context.MODE_PRIVATE)


        //listener for drawer menu logout
        requireActivity().nav_view.findViewById<TextView>(R.id.logout).setOnClickListener {
            val edit= sharedPref.edit()
            edit.clear()
            edit.apply()
            findNavController().navigate(R.id.action_mainMenuFragment_to_loginFragment)
        }

        //API
        val repository = ApiRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ApiViewModel::class.java)

        viewModel.getRestaurant()



        viewModel.response.observe(requireActivity(), Observer { restaurant ->
            var rest = restaurant
            Log.d("***",restaurant.toString())

            //RECYCLE
            var list = generateDummyList(30,rest)
            binding.recycleView.adapter = DataAdapter(list,requireActivity())
            binding.recycleView.layoutManager=LinearLayoutManager(context)
            binding.recycleView.setHasFixedSize(true)
        })



    }

    //TODO: DELETE THIS
    private fun generateDummyList(count: Int,item:Restaurant): List<Restaurant> {
        var list = mutableListOf<Restaurant>()
        for (i in 1..count){
           list.add(item)
        }
        return list
    }





}