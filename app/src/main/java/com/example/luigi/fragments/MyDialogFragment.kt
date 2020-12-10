package com.example.luigi.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luigi.R
import com.example.luigi.adapters.MainDataAdapter
import com.example.luigi.adapters.SelectDataAdapter
import com.example.luigi.databinding.FragmentMyDialogBinding
import com.example.luigi.viewModels.MyDatabaseViewModel

class MyDialogFragment : DialogFragment(), MainDataAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMyDialogBinding
    private lateinit var myDatabaseViewModel: MyDatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myDatabaseViewModel = requireActivity().run {
            ViewModelProvider(requireActivity()).get(MyDatabaseViewModel::class.java)
        }

        val recycle_view = binding.recycleView


        val adapter = SelectDataAdapter(myDatabaseViewModel.cityNames, this)
        recycle_view.adapter = adapter
        recycle_view.layoutManager = LinearLayoutManager(context)
        recycle_view.setHasFixedSize(true)



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                recycle_view.recycledViewPool.clear()
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })


    }

    override fun onItemClick(position: Int, cityName: String) {
        myDatabaseViewModel.currentCity.value = cityName
        dialog?.dismiss()
    }

    // ***************** PRIVATE FUNCTIONS *************


}