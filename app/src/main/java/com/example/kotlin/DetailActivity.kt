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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

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


        binding.tvTitle.text = title.toString()

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            getView(id)
            onClick(binding.ilanBilgileriButton)
        }

//        val response = ServiceBuilder.buildService().create(ServiceDetailInterface::class.java).getView(id).execute()
//        val responseBody = response.body()
//        if (responseBody != null) {
//            setOnClickListeners(responseBody)
//        }
//        onClick(binding.root)


        binding.ilanBilgileriButton.setOnClickListener {
            coroutineScope.launch {
                onClick(binding.ilanBilgileriButton)
            }
        }

        binding.aciklamaButton.setOnClickListener {
            coroutineScope.launch {
                onClick(binding.aciklamaButton)
            }
        }

        binding.kullaniciBilgileriButton.setOnClickListener {
            coroutineScope.launch {
                onClick(binding.kullaniciBilgileriButton)
            }
        }

    }

//    private fun setOnClickListeners(responseBody: ApiDetailResponse) {
//        val coroutineScope = CoroutineScope(Dispatchers.Main)
//        binding.ilanBilgileriButton.setOnClickListener {
//            coroutineScope.launch {
//                onClick(binding.ilanBilgileriButton)
//            }
//        }
//
//        binding.aciklamaButton.setOnClickListener {
//            coroutineScope.launch {
//                onClick(binding.aciklamaButton)
//            }
//        }
//
//        binding.kullaniciBilgileriButton.setOnClickListener {
//            coroutineScope.launch {
//                onClick(binding.kullaniciBilgileriButton)
//            }
//        }
//    }


    private suspend fun onClick(button: Button) {
        var bundle: Bundle = intent.extras!!

        val id: Int = bundle.getString("id")!!.toInt()
        val cardView = findViewById<CardView>(R.id.cardview)
        val carsDetailApi = ServiceBuilder.buildService().create(ServiceDetailInterface::class.java)
        val response = withContext(Dispatchers.IO) {
            carsDetailApi.getView(id).execute()
        }

        when (button.id) {
            R.id.ilan_bilgileri_button -> {
                // İlan bilgileri butonuna tıklanıldığında yapılacak işlemler
                val inflater = LayoutInflater.from(this)
                val ilanBilgileriView = inflater.inflate(R.layout.ilan_bilgileri, null)


                // İlgili TextView bileşenlerine metinleri atama
                val tvDate = ilanBilgileriView.findViewById<TextView>(R.id.tvDate)
                val tvModel = ilanBilgileriView.findViewById<TextView>(R.id.tvModel)
                val tvPrice = ilanBilgileriView.findViewById<TextView>(R.id.tvPrice)
                val tvGear=ilanBilgileriView.findViewById<TextView>(R.id.tvGear)
                val tvYear=ilanBilgileriView.findViewById<TextView>(R.id.tvYear)
                val tvKm=ilanBilgileriView.findViewById<TextView>(R.id.tvKm)
                val tvId= ilanBilgileriView.findViewById<TextView>(R.id.tvId)

                tvId.text = response.body()!!.id.toString()
                tvModel.text = response.body()!!.modelName
                tvPrice.text = response.body()!!.priceFormatted
                tvDate.text = response.body()!!.dateFormatted
                tvYear.text = response.body()!!.properties[2].value
                tvKm.text = response.body()!!.properties[0].value
                tvGear.text = response.body()!!.properties[3].value

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

                //öncelikle gelecek açıklama text i için içerisinden html etiketlerini temizliyorum ve html e çeviriyorum
                val cleanText = Jsoup.clean(response.body()!!.text, Whitelist.none())
                val doc: Document = Jsoup.parseBodyFragment(cleanText)

                tvExp.text = doc.body().html()

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

                tvId.text = response.body()!!.userInfo.id.toString()
                tvUserName.text = response.body()!!.userInfo.nameSurname
                tvUserPhone.text = response.body()!!.userInfo.phoneFormatted

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
