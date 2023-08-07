package com.example.kotlin.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlin.R
import com.example.kotlin.adapter.ImagePagerAdapter
import com.example.kotlin.dao.User
import com.example.kotlin.databinding.ActivityDetailBinding
import com.example.kotlin.model.ApiDetailResponse
import com.example.kotlin.model.UserInfo
import com.example.kotlin.viewModel.DetailViewModel
import com.example.kotlin.viewModel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by lazy {
        ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[DetailViewModel::class.java]
    }

    private val userViewModel by lazy {
        ViewModelProvider(
            this,
            defaultViewModelProviderFactory
        )[UsersViewModel::class.java]
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//        }
    }
    private var selectedButtonId: Int = 0
    private val selectedTextColor = Color.BLACK
    private val defaultTextColor = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        detailViewModel.getDetailDataObserve().observe(this) { data ->
            data.let {
                setParameters(data)
                setViewPager2Adapter(data)
                userInfoBtnClick(data.userInfo)
                userViewModel.loadRecords(data.userInfo.id)
            }

        }

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val bundle: Bundle = intent.extras!!
            val id: Int = bundle.getString("id")!!.toInt()
            binding.tvTitle.text = title.toString()

            detailViewModel.getView(id)
            onClick(binding.ilanBilgileriButton)
        }


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


    }

    private fun userInfoBtnClick(userInfo: UserInfo) {

        binding.kullaniciBilgileriButton.setOnClickListener {
            val cardView = findViewById<CardView>(R.id.cardview)
            val inflater = LayoutInflater.from(this)

            val userInformationView =
                inflater.inflate(R.layout.kullanici_bilgileri, null)

            // Kullanıcı bilgilerini Room'dan al
            userViewModel.getRecordsObserver().observe(this) { user ->
                if (user == null) {
                    setUserInfoToView(userInfo, userInformationView)

                } else {
                    Log.e("USERNAME FROM ROOM", user.nameSurname)
                    //  Veriler mevcutsa TextView'lara verileri ata
                    setUserToView(user, userInformationView)
                }
            }

            cardView.removeAllViews()
            cardView.addView(userInformationView)

            updateButtonSelection(binding.kullaniciBilgileriButton.id)
        }
    }

    //Room da yoksa user bilgileri room a kaydet ve göster
    private fun setUserInfoToView(userDetailInfo: UserInfo, userInformationView: View) {

        val userInfo = User(
            userDetailInfo.id,
            userDetailInfo.nameSurname,
            userDetailInfo.phoneFormatted,
            userDetailInfo.phone
        )
        if(userInfo !=null){
            userViewModel.addUser(userInfo)
            setUserToView(userInfo, userInformationView)
        }

    }

    private fun setUserToView(userDetailInfo: User?, kullaniciBilgileriView: View) {

        val tvPhoneFormatted = kullaniciBilgileriView.findViewById<TextView>(R.id.tvUserPhone)
        val tvUserName = kullaniciBilgileriView.findViewById<TextView>(R.id.textUserName)
        val tvId = kullaniciBilgileriView.findViewById<TextView>(R.id.tvId)

        tvId.text = userDetailInfo?.id.toString()
        tvUserName.text = userDetailInfo?.nameSurname
        tvPhoneFormatted.text = userDetailInfo?.phoneFormatted
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

    private fun onClick(button: Button) {
        val cardView = findViewById<CardView>(R.id.cardview)

        val userLiveData = detailViewModel.getDetailDataObserve()
        val inflater = LayoutInflater.from(this)

        userLiveData.observe(this) { user ->
            if (user != null) {
                when (button.id) {
                    R.id.ilan_bilgileri_button -> {
                        // İlan bilgileri butonuna tıklanıldığında yapılacak işlemler

                        val adInformationView = inflater.inflate(R.layout.ilan_bilgileri, null)

                        val tvDate = adInformationView.findViewById<TextView>(R.id.tvDate)
                        val tvModel = adInformationView.findViewById<TextView>(R.id.tvModel)
                        val tvPrice = adInformationView.findViewById<TextView>(R.id.tvPrice)
                        val tvGear = adInformationView.findViewById<TextView>(R.id.tvGear)
                        val tvYear = adInformationView.findViewById<TextView>(R.id.tvYear)
                        val tvKm = adInformationView.findViewById<TextView>(R.id.tvKm)
                        val tvId = adInformationView.findViewById<TextView>(R.id.tvId)
                        val tvFuel = adInformationView.findViewById<TextView>(R.id.tvFuel)

                        tvId.text = user.id.toString()
                        tvModel.text = user.modelName
                        tvPrice.text = user.priceFormatted
                        tvDate.text = user.dateFormatted
                        tvYear.text = user.properties[2].value
                        tvKm.text = user.properties[0].value
                        tvGear.text = user.properties[3].value
                        tvFuel.text = user.properties[4].value

                        println("PROPERTIESS EHE" + user.properties.toString())

                        cardView.removeAllViews()
                        cardView.addView(adInformationView)
                    }

                    R.id.aciklama_button -> {
                        val explanationView:View = inflater.inflate(R.layout.aciklama, null)

                        val tvExp = explanationView.findViewById<TextView>(R.id.tvExp)

                        //TODO deprecated olmuş kütüphane, değiştir
                        //öncelikle gelecek açıklama text i için içerisinden html etiketlerini temizliyorum ve html e çeviriyorum
                        val cleanText = Jsoup.clean(user.text, Whitelist.none())
                        val doc: Document = Jsoup.parseBodyFragment(cleanText)

                        tvExp.text = doc.body().html()

                        cardView.removeAllViews()
                        cardView.addView(explanationView)
                    }
                }
            }
        }
    }

    private fun setParameters(responseBody: ApiDetailResponse) {
        binding.tvTitle.text = responseBody.title
        binding.tvUserName.text = responseBody.userInfo.nameSurname
        binding.tvLocation.text = responseBody.location.cityName
    }
}


