package com.example.kotlin

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.viewpager.widget.ViewPager
import com.example.kotlin.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : AppCompatActivity() {
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


        var bundle: Bundle = intent.extras!!

        val id: Int = bundle.getString("id")!!.toInt()
        val title = bundle.getString("location")

        binding.tvTitle.text = title.toString()

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            getView(id)

        }

//        val response = ServiceBuilder.buildService().create(ServiceDetailInterface::class.java).getView(id).execute()
//        val responseBody = response.body()
//        if (responseBody != null) {
//            setOnClickListeners(responseBody)
//        }
//        onClick(binding.root)


        binding.ilanBilgileriButton.setOnClickListener {
            onClick(binding.ilanBilgileriButton)
        }

        binding.aciklamaButton.setOnClickListener {
            onClick(binding.aciklamaButton)
        }

        binding.kullaniciBilgileriButton.setOnClickListener {
            onClick(binding.kullaniciBilgileriButton)
        }

    }

//    private fun setOnClickListeners(responseBody: ApiDetailResponse) {
//        binding.ilanBilgileriButton.setOnClickListener {
//            onClick(binding.ilanBilgileriButton)
//        }
//
//        binding.aciklamaButton.setOnClickListener {
//            onClick(binding.aciklamaButton)
//        }
//
//        binding.kullaniciBilgileriButton.setOnClickListener {
//            onClick(binding.kullaniciBilgileriButton)
//        }
//    }

    private fun onClick(button: Button) {
        val cardView = findViewById<CardView>(R.id.cardview)

        when (button.id) {
            R.id.ilan_bilgileri_button -> {
                // İlan bilgileri butonuna tıklanıldığında yapılacak işlemler
                val inflater = LayoutInflater.from(this)
                val ilanBilgileriView = inflater.inflate(R.layout.ilan_bilgileri, null)


                // İlgili TextView bileşenlerine metinleri atama
                val tvDate = ilanBilgileriView.findViewById<TextView>(R.id.tvDate)
                val tvModel = ilanBilgileriView.findViewById<TextView>(R.id.tvModel)
                val tvPrice = ilanBilgileriView.findViewById<TextView>(R.id.tvPrice)


                tvModel.text = "responseBody.modelName"
                tvPrice.text = "responseBody.priceFormatted"
                tvDate.text = "responseBody.dateFormatted"


                // İlgili XML bileşenlerini ConstraintLayout içine ekleme
                cardView.removeAllViews()
                println("SGSGDSFSDFSD ${cardView.children}")
                cardView.addView(ilanBilgileriView)
                println("SARKI BİTTİ ${cardView.children.first().id}")
            }

            R.id.aciklama_button -> {
                // Açıklama butonuna tıklanıldığında yapılacak işlemler
                val inflater = LayoutInflater.from(this)
                val aciklamaView = inflater.inflate(R.layout.aciklama, null)

                // İlgili TextView bileşenlerine metinleri atayın
                val tvExp = aciklamaView.findViewById<TextView>(R.id.tvExp)


                tvExp.text = "responseBody.text.htmlEncode()"

                // İlgili XML bileşenini ConstraintLayout içine ekleme
                cardView.removeAllViews()
                cardView.addView(aciklamaView)
            }

            R.id.kullanici_bilgileri_button -> {
                // Kullanıcı bilgileri butonuna tıklanıldığında yapılacak işlemler
                val inflater = LayoutInflater.from(this)
                val kullaniciBilgileriView =
                    inflater.inflate(R.layout.kullanici_bilgileri, null)

                // İlgili TextView bileşenlerine metinleri atama
                val tvId = kullaniciBilgileriView.findViewById<TextView>(R.id.tvId)
                val tvUserName =
                    kullaniciBilgileriView.findViewById<TextView>(R.id.textUserName)
                val tvUserPhone =
                    kullaniciBilgileriView.findViewById<TextView>(R.id.tvUserPhone)

                tvId.text = "responseBody.userInfo.id.toString()"
                tvUserName.text = "responseBody.userInfo.nameSurname"
                tvUserPhone.text = "responseBody.userInfo.phoneFormatted"

                // İlgili XML bileşenlerini ConstraintLayout içine ekleme
                cardView.removeAllViews()
                cardView.addView(kullaniciBilgileriView)
            }
        }

    }

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
//      binding.tvModel.text = responseBody.modelName
//      binding.tvPrice.text = responseBody.priceFormatted
//      binding.tvDate.text = responseBody.dateFormatted
//      binding.tvExp.text=responseBody.text
//      binding.tvUserPhone.text=responseBody.userInfo.phoneFormatted
    }


}
