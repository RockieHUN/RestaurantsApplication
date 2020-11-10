package com.example.luigi.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.DataAdapter
import com.example.luigi.R
import com.example.luigi.databinding.FragmentMainMenuBinding
import com.example.luigi.model.Restaurant
import com.example.luigi.repository.ApiRepository
import com.example.luigi.viewModels.ApiViewModel
import com.example.luigi.viewModels.ApiViewModelFactory


class MainMenuFragment : Fragment() {

    private lateinit var binding : FragmentMainMenuBinding
    private lateinit var viewModel : ApiViewModel

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


        //API
        val repository = ApiRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ApiViewModel::class.java)

        viewModel.getRestaurant()

        var rest : Restaurant = Restaurant(1,"1","1","1","1","1",1,"1",
            "1",1.0,2.0,1.1,"1","1","1")

        viewModel.response.observe(requireActivity(), Observer { restaurant ->
            rest = restaurant
            Log.d("***",restaurant.toString())
        })

        //RECYCLE
        var list = generateDummyList(30,rest)
        binding.recycleView.adapter = DataAdapter(list)
        binding.recycleView.layoutManager=LinearLayoutManager(context)
        binding.recycleView.setHasFixedSize(true)

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