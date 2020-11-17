package com.example.luigi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.luigi.model.CityRestaurants
import com.example.luigi.model.Restaurant

class DataAdapter(
        private val items : List<Restaurant>,
        private  val listener : OnItemClickListener,
        private var liveData : LiveData<CityRestaurants>
) : RecyclerView.Adapter<DataAdapter.DataViewHolder>(){

    inner class DataViewHolder(itemView : View) : RecyclerView.ViewHolder (itemView), View.OnClickListener{
        val restaurantImage = itemView.findViewById<ImageView>(R.id.restaurant_image)
        val restaurantName = itemView.findViewById<TextView>(R.id.restaurant_name)
        val restaurantAddress = itemView.findViewById<TextView>(R.id.restaurant_address)
        val restaurantPrice = itemView.findViewById<TextView>(R.id.restaurant_price)




        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }



    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = items [position]
        holder.restaurantName.text = currentItem.name
        holder.restaurantAddress.text = currentItem.address
        holder.restaurantPrice.text = currentItem.price.toString()+"$"

        //TODO can I do better?
        //Glide.with(activity).load(currentItem.image_url).into(holder.restaurantImage)
    }

    override fun getItemCount(): Int = items.size

}