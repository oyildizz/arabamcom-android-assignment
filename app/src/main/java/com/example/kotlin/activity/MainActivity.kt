package com.example.kotlin.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.adapter.CarsListingAdapter
import com.example.kotlin.databinding.ActivityMainBinding
import com.example.kotlin.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val listViewModel by lazy{ ViewModelProvider(this,defaultViewModelProviderFactory)[ListViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // LiveData'ı observe ederek veri güncellemelerini otomatik olarak dinleme
        val adapter = CarsListingAdapter(emptyList(), this@MainActivity)
        binding.recyclerview.adapter = adapter

        listViewModel.getRecordObserver().observe(this) { cars ->
            Log.d("MainActivity", "cars LiveData updated, new data: $cars")
            if (cars.isNotEmpty()) {
                // Veriler varsa RecyclerView'i güncelle
                adapter.updateData(cars)
            } else {
                showErrorMessage("Veri bulunamadı veya bir hata oluştu.")
            }
        }
    }
    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

}