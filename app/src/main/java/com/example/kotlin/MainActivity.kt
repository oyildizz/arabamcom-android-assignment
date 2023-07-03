package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity()  {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)


        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            getAllProducts()
        }


        binding.button.setOnClickListener{
            val newIntent= Intent(this@MainActivity, DetailActivity::class.java)

            startActivity(newIntent)
        }

    }

//            binding.button.setOnClickListener{
//            if (binding.textInputEditText.text!!.isNotEmpty()){
//                var carName= binding.textInputEditText.text.toString()
//                when(carName){
//                    "hh"->binding.textView.text="OPEL ASTRA"
//                }
//            }
//            else{
//                binding.textView.text ="Herhangi bir araba modeli giriniz."
//            }
//        }


    private suspend fun getAllProducts() {
        println("INSIIDEEEEE")
        val carApi = ServiceBuilder.buildService().create(ServiceInterface::class.java)

        try {
            // Retrofit çağrısını coroutine içinde asenkron olarak yaptım
            val response = withContext(Dispatchers.IO) {
                carApi.getAllProducts().execute()
            }

            if (response.isSuccessful) {
                val adapter = PropertiesAdapter(response.body()!!)
                binding.recyclerview.adapter = adapter
                println("successs" + response.body()!!.first().photo.toString())
            } else {
                println("errorr" + response.message())
            }
        } catch (e: Exception) {
            println("errorr" + e.message)
        }
        }


//        retrofit.getAllProducts().enqueue(object : Callback<ApiResponse>{
//            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
////                try{
//                    //to avoid nulPointerException
//                    println("INSIDE RESPONSEEEEE")
////                    val responseBody= response.body()!!
////                    data = responseBody.products
//                    println("UPS OZİ BASTI" + response.toString())
////                    var adapter= PropertiesAdapter(data)
////                    binding.recyclerview.adapter=adapter
//
////                }
////                catch (ex: java.lang.Exception){
////                    println("HATA ALINDI")
////                    ex.printStackTrace()
////                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                t.printStackTrace()
//                Log.e("Failed", "Api Failed" + t.message)
//            }

//        })
    }


