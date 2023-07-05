package com.example.kotlin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PropertiesAdapter : RecyclerView.Adapter<PropertiesAdapter.ViewHolder>
     {

         private var mList: List<ApiResponse>
         private var context: Context?=null

         constructor(mList: List<ApiResponse>, context: Context) : super() {
             this.mList = mList
             this.context=context
         }



         override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.product_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val products = mList[position]
        holder.textView.setText(products.title)
        Picasso.get().load(products.photo.replace("{0}", "800x600")).into(holder.imageView)

        holder.imageView.setOnClickListener{
            var intent = Intent(context,DetailActivity::class.java)
            intent.putExtra("id",products.id.toString())
            intent.putExtra("title",products.title.toString())
            intent.putExtra("location",products.location.toString())
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return mList.size
    }


    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        val imageView: ImageView= ItemView.findViewById(R.id.imageView)
        val textView: TextView = ItemView.findViewById(R.id.textView)
    }




}