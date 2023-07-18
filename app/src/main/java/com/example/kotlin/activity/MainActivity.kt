package com.example.kotlin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.adapter.PropertiesAdapter
import com.example.kotlin.databinding.ActivityMainBinding
import com.example.kotlin.service.ServiceBuilder
import com.example.kotlin.api.ServiceInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
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
                val adapter = PropertiesAdapter(response.body()!!, this)
                binding.recyclerview.adapter = adapter
//                println("successs" + response.body()!!.first().photo.toString())
            } else {
                println("errorr" + response.message())
            }
        } catch (e: Exception) {
            println("errorr" + e.message)
        }
    }

}