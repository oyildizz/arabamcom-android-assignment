package com.example.kotlin.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin.adapter.CarsListingAdapter
import com.example.kotlin.databinding.ActivityMainBinding
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var carsObserver: Observer<List<ApiResponse>>

    // MainViewModel'in örneğini al
    private val listViewModel by lazy{ ViewModelProvider(this,defaultViewModelProviderFactory)[ListViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)



        // LiveData'ı observe ederek veri güncellemelerini otomatik olarak dinleme
        val adapter = CarsListingAdapter(emptyList(), this@MainActivity)
        binding.recyclerview.adapter = adapter

        carsObserver = Observer<List<ApiResponse>> { cars ->
            // LiveData'ın değiştiği zaman burası çalışır ve UI'ı günceller
            Log.d("MainActivity", "cars LiveData updated, new data: $cars")
//            adapter.updateData(cars) // Adapter'a yeni verileri atayarak güncelleme yapılır
            if (cars.isNotEmpty()) {
                // Veriler varsa RecyclerView'i güncelle
                adapter.updateData(cars)
            } else {
                // Veri yoksa veya hata oluştuysa kullanıcıya bildirim yap
                showErrorMessage("Veri bulunamadı veya bir hata oluştu.")
            }
        }

        listViewModel.cars.observe(this, carsObserver)
    }
    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        // "Veri bulunamadı" veya "Hata oluştu"
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