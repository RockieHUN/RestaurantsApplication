package com.example.luigi.adapters

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.luigi.R
import kotlinx.android.synthetic.main.select_recycle_item_layout.view.*

class SelectDataAdapter(
    private val items : List <String>,
    private val listener : MainDataAdapter.OnItemClickListener
): RecyclerView.Adapter<SelectDataAdapter.DataViewHolder>(){

    inner class DataViewHolder(itemView : View) : RecyclerView.ViewHolder (itemView), View.OnClickListener{
        val cityName: TextView = itemView.city_name

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.select_recycle_item_layout, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = items [position]
        holder.cityName.text = currentItem
    }

    override fun getItemCount(): Int = items.size
}