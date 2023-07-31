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
import com.example.kotlin.dao.User
import com.example.kotlin.dao.UsersViewModel
import com.example.kotlin.databinding.ActivityDetailBinding
import com.example.kotlin.model.ApiDetailResponse
import com.example.kotlin.service.ServiceBuilder
import com.example.kotlin.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

//    private val viewModel:UsersViewModel by viewModels()   --> viewModels() gelmiyor sebebini arastır

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailDataObserver: Observer<ApiDetailResponse>
    private lateinit var userViewModel: UsersViewModel

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }
    private var selectedButtonId: Int = 0
    private val selectedTextColor = Color.BLACK
    private val defaultTextColor = Color.WHITE
//    private var userInfo: User? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DetailViewModel'in örneğini aldım
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[DetailViewModel::class.java]

        userViewModel = ViewModelProvider(
            this
        )[UsersViewModel::class.java]

        // LiveData'nı observe etmek için Observer oluşturdum
        detailDataObserver = Observer { apiDetailResponse ->
            apiDetailResponse.let {
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
            val bundle: Bundle = intent.extras!!
            val id: Int = bundle.getString("id")!!.toInt()
            binding.tvTitle.text = title.toString()

            // Kullanıcı bilgilerini Room'dan al ve userData değişkenini güncelle
            setUpUserInfo()
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
            val cardView = findViewById<CardView>(R.id.cardview)
            val inflater = LayoutInflater.from(this)

            coroutineScope.launch {
                // Kullanıcı bilgileri butonuna tıklanıldığında yapılacak işlemler
                val kullaniciBilgileriView =
                    inflater.inflate(R.layout.kullanici_bilgileri, null)

                // Kullanıcı bilgilerini Room'dan al
                val user = userViewModel.getRecordsObserver().value
                Log.e("geTRECORDS", user.toString())
                if (user == null) {
                    // Veriler mevcut değilse API'den çek
                    userViewModel.fetchUserDataFromAPI(intent.extras!!)
                    Log.e("USERNAME IN IF USER==NULL", user?.nameSurname.toString())

                } else {
                    Log.e("USERNAME FROM ROOM", user.nameSurname)
                    // Veriler mevcutsa TextView'lara verileri ata
                    val tvId = kullaniciBilgileriView.findViewById<TextView>(R.id.tvId)
                    val tvUserName =
                        kullaniciBilgileriView.findViewById<TextView>(R.id.textUserName)
                    val tvUserPhone =
                        kullaniciBilgileriView.findViewById<TextView>(R.id.tvUserPhone)

                    tvId.text = user.id.toString()
                    tvUserName.text = user.nameSurname
                    tvUserPhone.text = user.phoneFormatted
                }

                // İlgili XML bileşenlerini ConstraintLayout içine ekleme
                cardView.removeAllViews()
                cardView.addView(kullaniciBilgileriView)

            }
            setUpUserInfo() // Kullanıcı bilgilerini veritabanına kaydet

            updateButtonSelection(binding.kullaniciBilgileriButton.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // LiveData'nın observe işlemini kaldırdım
        detailViewModel.detailData.removeObserver(detailDataObserver)
    }


    private fun setUpUserInfo() {
        userViewModel.getRecordsObserver().observe(this, Observer { user ->
            if (user != null) {
                // Kullanıcı bilgilerini al ve Room veritabanına kaydet
                val userInfo = User(user.id, user.nameSurname, user.phoneFormatted, user.phone)
                userViewModel.addUser(userInfo)
            }
        })
    }

//    private fun setUpUserInfo() {
//        userViewModel.getRecordsObserver().observe(this, Observer<User> {
//
//            if (it != null) {
//                val user = User(0, it.nameSurname, it.phoneFormatted, it.phone)
//                userViewModel.addUser(user)
//            } else {
//                println("NO DATA")
//
//            }
//        })
//    }

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
                val tvGear = ilanBilgileriView.findViewById<TextView>(R.id.tvGear)
                val tvYear = ilanBilgileriView.findViewById<TextView>(R.id.tvYear)
                val tvKm = ilanBilgileriView.findViewById<TextView>(R.id.tvKm)
                val tvId = ilanBilgileriView.findViewById<TextView>(R.id.tvId)
                val tvFuel = ilanBilgileriView.findViewById<TextView>(R.id.tvFuel)

                tvId.text = response.body()!!.id.toString()
                tvModel.text = response.body()!!.modelName
                tvPrice.text = response.body()!!.priceFormatted
                tvDate.text = response.body()!!.dateFormatted
                tvYear.text = response.body()!!.properties[2].value
                tvKm.text = response.body()!!.properties[0].value
                tvGear.text = response.body()!!.properties[3].value
                tvFuel.text = response.body()!!.properties[4].value

                println("PROPERTIESS EHE" + response.body()!!.properties.toString())

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
        }

    }

    private fun setParameters(responseBody: ApiDetailResponse) {
        binding.tvTitle.text = responseBody.title
        binding.tvUserName.text = responseBody.userInfo.nameSurname
        binding.tvLocation.text = responseBody.location.cityName
    }
}


