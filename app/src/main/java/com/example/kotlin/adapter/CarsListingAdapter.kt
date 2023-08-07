
package com.example.kotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.activity.DetailActivity
import com.example.kotlin.R
import com.squareup.picasso.Picasso

class CarsListingAdapter(private val context: Context) :
    PagingDataAdapter<ApiResponse, CarsListingAdapter.ViewHolder>(CarsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { product ->
            holder.bind(product)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val textViewLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val textViewPrice: TextView = itemView.findViewById(R.id.tvPrice)

        fun bind(product: ApiResponse) {
            textViewLocation.text = product.location.cityName
            textView.text = product.title
            textViewPrice.text = product.priceFormatted

            // Load product photo using Picasso
            Picasso.get().load(product.photo.replace("{0}", "800x600")).into(imageView)

            itemView.setOnClickListener {
                // Open DetailActivity when the item is clicked
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", product.id.toString())
                context.startActivity(intent)
            }
        }
    }
}

class CarsDiffCallback : DiffUtil.ItemCallback<ApiResponse>() {
    override fun areItemsTheSame(oldItem: ApiResponse, newItem: ApiResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ApiResponse, newItem: ApiResponse): Boolean {
        return oldItem == newItem
    }
}
