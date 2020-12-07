package com.example.luigi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.luigi.R
import com.example.luigi.model.RestaurantPicture
import com.example.luigi.viewModels.MyDatabaseViewModel

class ImageAdapter (
    private val items : MutableList<RestaurantPicture>,
    private  val listener : ImageAdapter.OnItemClickListener,
    private val myDatabaseViewModel: MyDatabaseViewModel

        ): RecyclerView.Adapter<ImageAdapter.DataViewHolder>(){

    inner class DataViewHolder(itemView : View) : RecyclerView.ViewHolder (itemView), View.OnClickListener{
        val imageView = itemView.findViewById<ImageView>(R.id.itemImage)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.DataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageAdapter.DataViewHolder, position: Int) {
        val currentItem = items [position]
        holder.imageView.setImageBitmap(items[position].image)

        //if its not the user's picture then he can't delete it
        if (myDatabaseViewModel.user.value!!.id != currentItem.userId) holder.deleteButton.visibility = View.GONE
    }

    override fun getItemCount(): Int = items.size

}