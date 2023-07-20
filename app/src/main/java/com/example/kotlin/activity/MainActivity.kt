package com.example.kotlin.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.adapter.CarsListingAdapter
import com.example.kotlin.databinding.ActivityMainBinding
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.viewModel.ListViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listViewModel: ListViewModel
    private lateinit var carsObserver: Observer<List<ApiResponse>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // MainViewModel'in örneğini al
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        // LiveData'ı observe ederek veri güncellemelerini otomatik olarak dinleme
        val adapter = CarsListingAdapter(emptyList(), this@MainActivity)
        binding.recyclerview.adapter = adapter

        carsObserver = Observer<List<ApiResponse>> { cars ->
            // LiveData'ın değiştiği zaman burası çalışır ve UI'ı günceller
            Log.d("MainActivity", "cars LiveData updated, new data: $cars")
            adapter.updateData(cars) // Adapter'a yeni verileri atayarak güncelleme yapılır
        }

        listViewModel.cars.observe(this, carsObserver)
    }

//    removeObserver kullanılarak LiveData'ya olan aboneliği kaldırır. Bu, Activity yok
//    edildiğinde sızıntıları önlemeye yardımcı olur ve Observer'ın artık LiveData'yı
//    dinlememesini sağlar. Böylece, veriler güncellendiğinde MainActivity de bu değişiklikleri
//    alabilecektir.
    override fun onDestroy() {
        super.onDestroy()
        // Observer'ı kaldırarak sızıntıları önlemek için
        listViewModel.cars.removeObserver(carsObserver)
    }
}