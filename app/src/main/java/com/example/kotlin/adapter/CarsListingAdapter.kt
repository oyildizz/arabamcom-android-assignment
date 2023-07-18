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

class PropertiesAdapter(private var mList: List<ApiResponse>, context: Context) :
    RecyclerView.Adapter<PropertiesAdapter.ViewHolder>() {

    private var context: Context? = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val products = mList[position]
        holder.textViewLocation.text = products.location.cityName
        holder.textView.text = products.title
        holder.textViewPrice.text = products.priceFormatted
        Picasso.get().load(products.photo.replace("{0}", "800x600")).into(holder.imageView)

        holder.imageView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("id", products.id.toString())
            intent.putExtra("title", products.title)
            intent.putExtra("location", products.location.toString())
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = ItemView.findViewById(R.id.imageView)
        val textView: TextView = ItemView.findViewById(R.id.textView)
        val textViewLocation: TextView = ItemView.findViewById(R.id.tvLocation)
        val textViewPrice: TextView=ItemView.findViewById(R.id.tvPrice)
    }
}