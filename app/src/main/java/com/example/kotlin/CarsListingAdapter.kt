package com.example.kotlin

import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PropertiesAdapter(private val mList:List<ApiResponse>):RecyclerView.Adapter<PropertiesAdapter.ViewHolder>()
     {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.product_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val products = mList[position]
        holder.textView.setText(products.title)
        Picasso.get().load(products.photo.replace("{0}", "800x600")).into(holder.imageView)
    }

    override fun getItemCount(): Int {
       return mList.size
    }


    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        val imageView: ImageView= ItemView.findViewById(R.id.imageView)
        val textView: TextView = ItemView.findViewById(R.id.textView)
    }




}