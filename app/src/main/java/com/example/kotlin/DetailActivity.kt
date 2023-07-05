package com.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.databinding.ActivityDetailBinding
import com.example.kotlin.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        var bundle: Bundle=intent.extras!!

        val id: Int= bundle.getString("id")!!.toInt()
        val title= bundle.getString("location")

        binding.textViewTitle.text=title.toString()

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            getView(id)

        }
    }

    private suspend fun getView(id:Int) {

        val carDetailApi = ServiceBuilder.buildService().create(ServiceDetailInterface::class.java)
        println("ID NEDİR BUDUR"+ id)
        try {
            val response = withContext(Dispatchers.IO) {
                carDetailApi.getView(id).execute()
            }
            if (response.isSuccessful) {
                val responseBody = response.body()
                println("response"+ responseBody)
                if (responseBody != null) {
//                    val adapter = CarsDetailAdapter(responseBody, this)

                    setParametres(responseBody)

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
      fun setParametres(responseBody: ApiDetailResponse){
          Picasso.get().load(responseBody.photos[0].replace("{0}", "800x600")).into(binding.imageViewDetail)
          binding.textViewTitle.text=responseBody.title
          binding.tvUserName.text=responseBody.userInfo.nameSurname
          binding.tvLocation.text=responseBody.location.cityName
          binding.tvModel.text=responseBody.modelName
          binding.tvPrice.text=responseBody.priceFormatted
          binding.tvDate.text=responseBody.dateFormatted
//          binding.tvExp.text=responseBody.text
          binding.tvUserPhone.text=responseBody.userInfo.phoneFormatted
      }

}

