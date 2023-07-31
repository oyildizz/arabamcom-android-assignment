package com.example.kotlin.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.repo.CarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val carRepository: CarsRepository ): ViewModel() {
     private var _cars : MutableLiveData<List<ApiResponse>> = MutableLiveData()
    val cars: MutableLiveData<List<ApiResponse>> get() = _cars

    private val carsError = MutableLiveData<Boolean>()
    private val carsLoading = MutableLiveData<Boolean>()

    init {
        // ViewModel oluşturulduğunda verileri hemen al
        getAllProducts()
    }

    fun getRecordObserver():MutableLiveData<List<ApiResponse>>{
        return _cars
    }

    private fun getAllProducts() {
        carsLoading.postValue(true)
        Log.d("ListViewModel", "getAllProducts() called")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = carRepository.getAllProducts()
                if (data.isNotEmpty()) {
                    // Veriler varsa, LiveData'ı güncelle
                    _cars.postValue(data)
                    carsError.postValue(false)
                    Log.d("ListViewModel", "getAllProducts() success, data: $data")
                } else {
                    // Veri yoksa, hatayı işaretle ve gerekirse kullanıcıya bildir
                    _cars.postValue(emptyList())
                    carsError.postValue(true)
                    Log.e("ListViewModel", "getAllProducts() error: No data found.")
                }
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