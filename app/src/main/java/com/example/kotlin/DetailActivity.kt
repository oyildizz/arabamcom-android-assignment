package com.example.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.kotlin.databinding.ActivityDetailBinding
import com.example.kotlin.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private var context: Context? = null

    private lateinit var viewPager: ViewPager
    private lateinit var handler: Handler
    private lateinit var photos: ArrayList<String>
    private lateinit var adapter: ImagePagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        var bundle: Bundle = intent.extras!!

        val id: Int = bundle.getString("id")!!.toInt()
        val title = bundle.getString("location")

        binding.tvTitle.text = title.toString()

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            getView(id)

        }

//        val ilanBilgileriButton: Button = findViewById(R.id.ilan_bilgileri_button)
//        val aciklamaButton: Button = findViewById(R.id.aciklama_button)
//        val kullaniciBilgileriButton: Button = findViewById(R.id.kullanici_bilgileri_button)
//
//
//        ilanBilgileriButton.setOnClickListener {
//
//            var intent = Intent(context,MainActivity::class.java)
//            context!!.startActivity(intent)
//        }
//
//        aciklamaButton.setOnClickListener {
//
//        }
//
//        kullaniciBilgileriButton.setOnClickListener {
//
//        }

    }


    @SuppressLint("MissingInflatedId")
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ilan_bilgileri_button -> {
                // İlan bilgileri butonuna tıklanıldığında yapılacak işlemler
                val inflater = LayoutInflater.from(this)
                val ilanBilgileriView = inflater.inflate(R.layout.ilan_bilgileri, null)

                // İlgili TextView bileşenlerine metinleri atama
                val tvDate = ilanBilgileriView.findViewById<TextView>(R.id.tvDate)
                val tvModel = ilanBilgileriView.findViewById<TextView>(R.id.tvModel)
                val tvPrice = ilanBilgileriView.findViewById<TextView>(R.id.tvPrice)


                tvModel.text = "MODEL"
                tvPrice.text = "10.000 TL"
                tvDate.text = "20 HAZİRAN"


                // İlgili XML bileşenlerini ConstraintLayout içine ekleme
                val constraintLayout = findViewById<ConstraintLayout>(R.id.recyclerview)
                constraintLayout.removeAllViews()
                constraintLayout.addView(ilanBilgileriView)
            }

            R.id.aciklama_button -> {
                // Açıklama butonuna tıklanıldığında yapılacak işlemler
                val inflater = LayoutInflater.from(this)
                val aciklamaView = inflater.inflate(R.layout.aciklama, null)

                // İlgili TextView bileşenlerine metinleri atayın
                val tvExp = aciklamaView.findViewById<TextView>(R.id.tvExp)


                tvExp.text = "Açıklama Başlık"

                // İlgili XML bileşenini ConstraintLayout içine ekleme
                val constraintLayout = findViewById<ConstraintLayout>(R.id.recyclerview)
                constraintLayout.removeAllViews()
                constraintLayout.addView(aciklamaView)
            }

            R.id.kullanici_bilgileri_button -> {
                // Kullanıcı bilgileri butonuna tıklanıldığında yapılacak işlemler
                val inflater = LayoutInflater.from(this)
                val kullaniciBilgileriView =
                    inflater.inflate(R.layout.kullanici_bilgileri, null)

                // İlgili TextView bileşenlerine metinleri atama
                val tvId = kullaniciBilgileriView.findViewById<TextView>(R.id.tvId)
                val tvUserName = kullaniciBilgileriView.findViewById<TextView>(R.id.tvUserName)
                val tvUserPhone = kullaniciBilgileriView.findViewById<TextView>(R.id.tvUserPhone)

                tvId.text = "Kullanıcı Başlık"
                tvUserName.text = "Kullanıcı Adı"
                tvUserPhone.text = "Kullanıcı Lokasyon"

                // İlgili XML bileşenlerini ConstraintLayout içine ekleme
                val constraintLayout = findViewById<ConstraintLayout>(R.id.recyclerview)
                constraintLayout.removeAllViews()
                constraintLayout.addView(kullaniciBilgileriView)
            }
        }
    }

//    private fun init(){
//        viewPager = findViewById (R.id.viewPager)
//        handler = Handler(Looper.myLooper()!!)
//        photos = ArrayList()
//        photos.add (R.drawable.one)
//
//        adapter =ImagePagerAdapter(photos,viewPager)
//        viewPager.adapter=adapter
//        viewPager.offscreenPageLimit=3
//        viewPager.clipToPadding=false
//        viewPager.clipChildren=false
//        viewPager.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
//
//    }

    private suspend fun getView(id: Int) {

        val carDetailApi = ServiceBuilder.buildService().create(ServiceDetailInterface::class.java)
        println("ID NEDİR BUDUR" + id)
        try {
            val response = withContext(Dispatchers.IO) {
                carDetailApi.getView(id).execute()
            }
            if (response.isSuccessful) {
                val responseBody = response.body()
                println("response" + responseBody)
                if (responseBody != null) {
                    setParameters(responseBody)
                    println("successs" + responseBody.userInfo.nameSurname)
                } else {
                    println("Response body is null in detail.")
                }
            } else {
                println("errorr" + response.message())
            }
        } catch (e: Exception) {
            println("errorr" + e.message)
        }
    }

    fun setParameters(responseBody: ApiDetailResponse) {
        Picasso.get().load(responseBody.photos[0].replace("{0}", "800x600"))
            .into(binding.imageViewDetail)
        binding.tvTitle.text = responseBody.title
        binding.tvUserName.text = responseBody.userInfo.nameSurname
        binding.tvLocation.text = responseBody.location.cityName
//        binding.tvModel.text = responseBody.modelName
//        binding.tvPrice.text = responseBody.priceFormatted
//        binding.tvDate.text = responseBody.dateFormatted
//          binding.tvExp.text=responseBody.text
//          binding.tvUserPhone.text=responseBody.userInfo.phoneFormatted
    }


}

