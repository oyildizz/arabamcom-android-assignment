package com.example.kotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.activity.DetailActivity
import com.example.kotlin.R
import com.squareup.picasso.Picasso

class CarsListingAdapter(private var mList: List<ApiResponse>, private val context: Context) :
    RecyclerView.Adapter<CarsListingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = mList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateData(newList: List<ApiResponse>) {
        mList = newList
        notifyDataSetChanged()
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