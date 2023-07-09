package com.example.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.*

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
    }

    private suspend fun getAllProducts() {
        val carApi = ServiceBuilder.buildService().create(ServiceInterface::class.java)

        try {
            // Retrofit çağrısını coroutine içinde asenkron olarak yaptım
            val response = withContext(Dispatchers.IO) {
                carApi.getAllProducts().execute()
            }

            if (response.isSuccessful) {
                val adapter = PropertiesAdapter(response.body()!!,this)
                binding.recyclerview.adapter = adapter
//                println("successs" + response.body()!!.first().photo.toString())
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
//                    println("ups" + response.toString())
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