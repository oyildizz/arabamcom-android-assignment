package com.example.kotlin.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.model.ApiDetailResponse
import com.example.kotlin.repo.CarsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {
    private val carRepository = CarsRepository()
    private val _detailData = MutableLiveData<ApiDetailResponse>()
    val detailData: MutableLiveData<ApiDetailResponse> get() = _detailData

    private val detailDataError = MutableLiveData<Boolean>()
    private val detailDataLoading = MutableLiveData<Boolean>()

    suspend fun getView(id: Int) {
        try {
            detailDataLoading.postValue(true)

            val response = withContext(Dispatchers.IO) {
                carRepository.getProductDetail(id)
            }

            _detailData.postValue(response)
            detailDataError.postValue(false)
        } catch (e: Exception) {
            _detailData.postValue(null)
            detailDataError.postValue(true)
        } finally {
            detailDataLoading.postValue(false)
        }
    }
}
