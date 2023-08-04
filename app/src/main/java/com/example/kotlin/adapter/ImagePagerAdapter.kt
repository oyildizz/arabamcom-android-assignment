package com.example.kotlin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.databinding.ItemImageLayoutBinding
import com.squareup.picasso.Picasso

class ImagePagerAdapter(private val images: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>(){
    class ViewHolder(val binding:ItemImageLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val photoUrl = images[position]

       val fixedUrl =  photoUrl.replace("{0}", "800x600")
        Log.e("images", photoUrl)
        Log.e("images2", fixedUrl)

        Picasso.get().load(fixedUrl).into(holder.binding.imageViewDetail)
    }
    override fun getItemCount(): Int {
        return images.size
    }
}
