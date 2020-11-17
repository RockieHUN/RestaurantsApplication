package com.example.luigi.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.DataAdapter
import com.example.luigi.R
import com.example.luigi.databinding.FragmentMainMenuBinding
import com.example.luigi.repository.ApiRepository
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.ApiViewModelFactory
import com.example.luigi.viewModels.MyDatabaseViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainMenuFragment : Fragment(),  DataAdapter.OnItemClickListener {

    private lateinit var binding : FragmentMainMenuBinding
    private lateinit var apiViewModel : ApiViewModel
    private lateinit var sharedPref : SharedPreferences
    private lateinit var myDatabaseViewModel : MyDatabaseViewModel

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

        //API VIEWMODEL
        val repository = ApiRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        apiViewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(ApiViewModel::class.java)

        //DATABASE VIEWMODEL
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }

        Log.d("**********",myDatabaseViewModel.restaurants.value.toString())
        
        myDatabaseViewModel.restaurants.observe(requireActivity(), Observer { restaurants ->

            //RECYCLE VIEW
            binding.recycleView.adapter = DataAdapter(restaurants,this)
            binding.recycleView.layoutManager=LinearLayoutManager(context)
            binding.recycleView.setHasFixedSize(true)
        })



    }

    override fun onItemClick(position: Int) {
        val bundle = bundleOf(Pair("position",position))
        findNavController().navigate(R.id.action_mainMenuFragment_to_detailFragment,bundle)
    }









}