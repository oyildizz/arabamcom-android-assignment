package com.example.kotlin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.databinding.ItemImageLayoutBinding
import com.squareup.picasso.Picasso

class ImagePagerAdapter(private val images: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ViewHolder>() {
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




























//class com.example.kotlin.adapter.ImagePagerAdapter(private val context: Context, private val photos: ArrayList<String>, private val viewPager: ViewPager) :RecyclerView.Adapter<com.example.kotlin.adapter.ImagePagerAdapter.ImageViewHolder>() {
//
//
//    class ImageViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
//        val imageView: ImageView = itemView.findViewById(R.id.imageViewDetail)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view=LayoutInflater.from(parent.context).inflate(R.layout.activity_detail, parent, false)
//    return  ImageViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        holder.imageView.setImageResource(photos[position].toInt())
//        if(position==photos.size-1){
//            viewPager.post(runnable)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return photos.size
//    }
//
//    private val runnable=Runnable{
////        val tempPhotos = ArrayList<String>(photos) // photos listesini geçici bir liste oluşturuyoruz
////        photos.clear() // orijinal listeyi temizliyoruz
//        photos.addAll(photos) // geçici listeyle orijinal listeyi birleştiriyoruz
//        notifyDataSetChanged()
//    }






//-----------------------------------------------------------------------------------------------------------






//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val inflater = LayoutInflater.from(context)
//        val itemView = inflater.inflate(R.layout.activity_detail, container, false)
//        val imageView = itemView.findViewById<ImageView>(R.id.imageViewDetail)
//        Picasso.get().load(photos[position]).into(imageView)
//        container.addView(itemView)
//        return itemView
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
//        container.removeView(obj as View)
//    }
//
//    override fun getCount(): Int {
//        return photos.size
//    }
//
//    override fun isViewFromObject(view: View, obj: Any): Boolean {
//        return view == obj
//    }
//}

