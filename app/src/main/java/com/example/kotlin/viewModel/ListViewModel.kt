package com.example.kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.repo.CarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val carRepository: CarsRepository) : ViewModel() {

    var carList: Flow<PagingData<ApiResponse>>? = null


    fun getAllProducts() {
        Log.d("ListViewModel", "getAllProducts() called")
        val data = carRepository.getAllProducts().cachedIn(viewModelScope)
        carList = data
        Log.d("ListViewModel", "getAllProducts() completed")

    }
}