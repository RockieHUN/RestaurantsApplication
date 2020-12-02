package com.example.luigi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.luigi.R
import com.example.luigi.room.entities.EntityRestaurant
import com.example.luigi.viewModels.MyDatabaseViewModel

class MainDataAdapter(
    private val items : List<EntityRestaurant>,
    private  val listener : OnItemClickListener,
    private val myDatabaseViewModel: MyDatabaseViewModel
) : RecyclerView.Adapter<MainDataAdapter.DataViewHolder>(){

    inner class DataViewHolder(itemView : View) : RecyclerView.ViewHolder (itemView), View.OnClickListener{
        val restaurantImage = itemView.findViewById<ImageView>(R.id.restaurant_image)
        val restaurantName = itemView.findViewById<TextView>(R.id.restaurant_name)
        val restaurantAddress = itemView.findViewById<TextView>(R.id.restaurant_address)
        val restaurantPrice = itemView.findViewById<TextView>(R.id.restaurant_price)
        val likeButton = itemView.findViewById<ImageButton>(R.id.favoriteButton)


        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position,"")
            }
        }


    }
    interface OnItemClickListener{
        fun onItemClick(position: Int, toString: String)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_recycle_item_layout,parent,false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = items [position]
        holder.restaurantName.text = currentItem.name
        holder.restaurantAddress.text = currentItem.address
        holder.restaurantPrice.text = currentItem.price.toString()+"$"

        holder.likeButton.setOnClickListener {
            myDatabaseViewModel.like(currentItem)
        }


        //TODO can I do better?
        //Glide.with(activity).load(currentItem.image_url).into(holder.restaurantImage)
    }



    override fun getItemCount(): Int = items.size

}