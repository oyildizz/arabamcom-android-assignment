package com.example.kotlin
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso


class ImagePagerAdapter(private val context: Context, private val photos: ArrayList<String>, private val viewPager: ViewPager) :RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {


    class ImageViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.imageViewDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.activity_detail, parent, false)
    return  ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(photos[position].toInt())
        if(position==photos.size-1){
            viewPager.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    private val runnable=Runnable{
//        val tempPhotos = ArrayList<String>(photos) // photos listesini geçici bir liste oluşturuyoruz
//        photos.clear() // orijinal listeyi temizliyoruz
        photos.addAll(photos) // geçici listeyle orijinal listeyi birleştiriyoruz
        notifyDataSetChanged()
    }






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
}

