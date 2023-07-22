package com.example.kotlin.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.repo.CarsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val carRepository = CarsRepository()
    private val _cars = MutableLiveData<List<ApiResponse>>()
    val cars: MutableLiveData<List<ApiResponse>> get() = _cars

    private val carsError = MutableLiveData<Boolean>()
    private val carsLoading = MutableLiveData<Boolean>()

    init {
        // ViewModel oluşturulduğunda verileri hemen alalım
        getAllProducts()
    }

    private fun getAllProducts() {
        carsLoading.postValue(true)
        Log.d("ListViewModel", "getAllProducts() called")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = carRepository.getAllProducts()
                _cars.postValue(data)
                carsError.postValue(false)
                Log.d("ListViewModel", "getAllProducts() success, data: $data")
            } catch (e: Exception) {
                _cars.postValue(emptyList())
                carsError.postValue(true)
                Log.e("ListViewModel", "getAllProducts() error: ${e.message}")
            } finally {
                carsLoading.postValue(false)
                Log.d("ListViewModel", "getAllProducts() completed")
            }
        }
    }
}