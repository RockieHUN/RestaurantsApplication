package com.example.luigi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.luigi.R
import kotlinx.android.synthetic.main.select_recycle_item_layout.view.*
import java.util.*

class SelectDataAdapter(
    private val items : List <String>,
    private val listener : MainDataAdapter.OnItemClickListener
): RecyclerView.Adapter<SelectDataAdapter.DataViewHolder>(), Filterable{

    var filteredList = items

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
        val currentItem = filteredList [position]
        holder.cityName.text = currentItem
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                //if search is empty return all city name
                if (charSearch.isEmpty()) {
                    filteredList = items
                }
                else {

                    //filter city names
                     filteredList = items.filter { item ->
                        item.toLowerCase(Locale.ROOT)
                            .contains(constraint.toString().toLowerCase(Locale.ROOT))
                    }.toMutableList()

                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<String>
                notifyDataSetChanged()
            }
        }
    }


}