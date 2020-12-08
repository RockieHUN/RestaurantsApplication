package com.example.luigi.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.luigi.R
import com.example.luigi.model.RestaurantWithPicture
import com.example.luigi.room.entities.EntityRestaurant
import com.example.luigi.viewModels.MyDatabaseViewModel
import java.util.*

class MainDataAdapter(
    private val items : List<RestaurantWithPicture>,
    private  val listener : OnItemClickListener,
    private val myDatabaseViewModel: MyDatabaseViewModel
) : RecyclerView.Adapter<MainDataAdapter.DataViewHolder>(), Filterable{

    var filteredList = items

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
                listener.onItemClick(position, restaurantName.text.toString())
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
        val currentItem = filteredList [position]
        holder.restaurantName.text = currentItem.name
        holder.restaurantAddress.text = currentItem.address
        holder.restaurantPrice.text = currentItem.price.toString()+"$"

        //if the image is null keep the placeholder
        if (currentItem.image!=null)  holder.restaurantImage.setImageBitmap(currentItem.image)


        if (isLiked(currentItem.id)){
            holder.likeButton.setColorFilter(Color.argb(255, 194, 39, 72))
        }


        holder.likeButton.setOnClickListener {
            myDatabaseViewModel.like(currentItem)

            if (isLiked(currentItem.id)){
                holder.likeButton.setColorFilter(Color.argb(255,36, 189, 28))
            }
            else{
                holder.likeButton.setColorFilter(Color.argb(255, 194, 39, 72))
            }
        }


        //TODO can I do better?
        //Glide.with(activity).load(currentItem.image_url).into(holder.restaurantImage)
    }

    override fun getItemCount(): Int = filteredList.size

    private fun isLiked(currentItemId : Int): Boolean{
        val filtered = myDatabaseViewModel.favorites.value?.filter{favorite ->
            favorite.restaurantId == currentItemId
        }
        if (filtered == null) return false
        else{
            return filtered.isNotEmpty()
        }

    }


    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                //if search is empty return all restaurant
                if (charSearch.isEmpty()) {
                    filteredList = items
                }
                else {

                    //filter restaurants by name
                    filteredList = items.filter { item ->
                        item.name.toLowerCase(Locale.ROOT)
                            .contains(constraint.toString().toLowerCase(Locale.ROOT))
                    }.toMutableList()

                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<RestaurantWithPicture>
                notifyDataSetChanged()
            }
        }
    }

}