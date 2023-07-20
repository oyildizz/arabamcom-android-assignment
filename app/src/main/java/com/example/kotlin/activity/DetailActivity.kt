package com.example.kotlin.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlin.R
import com.example.kotlin.adapter.ImagePagerAdapter
import com.example.kotlin.api.ServiceDetailInterface
import com.example.kotlin.databinding.ActivityDetailBinding
import com.example.kotlin.model.ApiDetailResponse
import com.example.kotlin.service.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.kotlin.viewModel.DetailViewModel
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailDataObserver: Observer<ApiDetailResponse>


    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }
    private var selectedButtonId: Int = 0
    private val selectedTextColor = Color.BLACK
    private val defaultTextColor = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle = intent.extras!!
        val id: Int = bundle.getString("id")!!.toInt()
        binding.tvTitle.text = title.toString()

        // DetailViewModel'in örneğini aldım
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        // LiveData'nı observe etmek için Observer oluşturdum
        detailDataObserver = Observer { apiDetailResponse ->
            apiDetailResponse?.let {
                setParameters(apiDetailResponse)
                setViewPager2Adapter(apiDetailResponse)
            }
            Log.d("MainActivity", "carsDetail LiveData updated, new data: $apiDetailResponse")
        }
        // LiveData'yı observe ettim
        detailViewModel.detailData.observe(this, detailDataObserver)

        // Verileri API'den al ve UI'ı güncellemek için LiveData'yı gözlemledim
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            detailViewModel.getView(id)
            onClick(binding.ilanBilgileriButton)
        }

//        // Observe the detailData LiveData to update UI when data is received
//        detailViewModel.detailData.observe(this) { apiDetailResponse ->
//            setParameters(apiDetailResponse)
//            setViewPager2Adapter(apiDetailResponse)
//        }

        binding.ilanBilgileriButton.setOnClickListener {
            coroutineScope.launch {
                onClick(binding.ilanBilgileriButton)
            }
            updateButtonSelection(binding.ilanBilgileriButton.id)
        }

        binding.aciklamaButton.setOnClickListener {
            coroutineScope.launch {
                onClick(binding.aciklamaButton)
            }
            updateButtonSelection(binding.aciklamaButton.id)
        }

        binding.kullaniciBilgileriButton.setOnClickListener {
            coroutineScope.launch {
                onClick(binding.kullaniciBilgileriButton)
            }
            updateButtonSelection(binding.kullaniciBilgileriButton.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // LiveData'nın observe işlemini kaldırdım
        detailViewModel.detailData.removeObserver(detailDataObserver)
    }

        private fun updateButtonSelection(selectedId: Int) {
        // Seçilen butonun rengini değiştir
        val selectedButton = findViewById<Button>(selectedId)
        selectedButton.setBackgroundResource(R.drawable.selected_button_background)
        selectedButton.setTextColor(selectedTextColor)

        // Daha önce seçilen butonun rengini eski haline getir
        if (selectedButtonId != 0 && selectedButtonId != selectedId) {
            val previousButton = findViewById<Button>(selectedButtonId)
            previousButton.setBackgroundResource(R.drawable.button_background)
            previousButton.setTextColor(defaultTextColor)
        }

        // Seçili butonun ID'sini güncelle
        selectedButtonId = selectedId
    }

    private fun setImagePagerAdapter(responseBody: ApiDetailResponse): ImagePagerAdapter {
        return ImagePagerAdapter(responseBody.photos)
    }

    private fun setViewPager2Adapter(responseBody: ApiDetailResponse) {
        //TODO: scoope functions
        with(binding) {
            viewPager2.adapter = setImagePagerAdapter(responseBody)
            viewPager2.registerOnPageChangeCallback(pageChangeCallback)
        }
    }
    @SuppressLint("InflateParams")
    private suspend fun onClick(button: Button) {
        val bundle: Bundle = intent.extras!!

        val id: Int = bundle.getString("id")!!.toInt()
        val cardView = findViewById<CardView>(R.id.cardview)
        val carsDetailApi = ServiceBuilder.buildService().create(ServiceDetailInterface::class.java)
        val response = withContext(Dispatchers.IO) {
            carsDetailApi.getView(id).execute()
        }
        val inflater = LayoutInflater.from(this)
        when (button.id) {
            R.id.ilan_bilgileri_button -> {
                // İlan bilgileri butonuna tıklanıldığında yapılacak işlemler

                val ilanBilgileriView = inflater.inflate(R.layout.ilan_bilgileri, null)


                // İlgili TextView bileşenlerine metinleri atama
                val tvDate = ilanBilgileriView.findViewById<TextView>(R.id.tvDate)
                val tvModel = ilanBilgileriView.findViewById<TextView>(R.id.tvModel)
                val tvPrice = ilanBilgileriView.findViewById<TextView>(R.id.tvPrice)
                val tvGear=ilanBilgileriView.findViewById<TextView>(R.id.tvGear)
                val tvYear=ilanBilgileriView.findViewById<TextView>(R.id.tvYear)
                val tvKm=ilanBilgileriView.findViewById<TextView>(R.id.tvKm)
                val tvId= ilanBilgileriView.findViewById<TextView>(R.id.tvId)
                val tvFuel=ilanBilgileriView.findViewById<TextView>(R.id.tvFuel)

                tvId.text = response.body()!!.id.toString()
                tvModel.text = response.body()!!.modelName
                tvPrice.text = response.body()!!.priceFormatted
                tvDate.text = response.body()!!.dateFormatted
                tvYear.text = response.body()!!.properties[2].value
                tvKm.text = response.body()!!.properties[0].value
                tvGear.text = response.body()!!.properties[3].value
                tvFuel.text=response.body()!!.properties[4].value

                println("PROPERTIESS EHE"+ response.body()!!.properties.toString())

                // İlgili XML bileşenlerini ConstraintLayout içine ekleme
                cardView.removeAllViews()
                println("SGSGDSFSDFSD ${cardView.children}")
                cardView.addView(ilanBilgileriView)
                println("SARKI BİTTİ ${cardView.children.first().id}")
            }

            R.id.aciklama_button -> {
                // Açıklama butonuna tıklanıldığında yapılacak işlemler
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

    private fun setParameters(responseBody: ApiDetailResponse) {
        binding.tvTitle.text = responseBody.title
        binding.tvUserName.text = responseBody.userInfo.nameSurname
        binding.tvLocation.text = responseBody.location.cityName
    }

}


