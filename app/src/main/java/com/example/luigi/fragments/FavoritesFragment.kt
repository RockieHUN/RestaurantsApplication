package com.example.luigi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.R
import com.example.luigi.adapters.FavoriteDataAdapter
import com.example.luigi.adapters.MainDataAdapter
import com.example.luigi.databinding.FragmentFavoritesBinding
import com.example.luigi.utils.ClassConverter
import com.example.luigi.viewModels.MyDatabaseViewModel
import java.util.*
import kotlin.concurrent.timerTask


class FavoritesFragment : Fragment(), FavoriteDataAdapter.OnItemClickListener {

    private lateinit var myDatabaseViewModel : MyDatabaseViewModel
    private lateinit var binding : FragmentFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorites,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //onBackPressed navigate to the main fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_favoritesFragment_to_mainMenuFragment)
        }

        //DATABASE VIEWMODEL
        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }

        //recycle view
        binding.recycleView.adapter = FavoriteDataAdapter(
           myDatabaseViewModel.favorites.value!!,
            this, myDatabaseViewModel,
            viewLifecycleOwner)


        binding.recycleView.layoutManager = LinearLayoutManager(context)
        binding.recycleView.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int, restaurantName: String) {
        myDatabaseViewModel.restaurantName = restaurantName
        findNavController().navigate(R.id.action_favoritesFragment_to_detailFragment)
    }


}