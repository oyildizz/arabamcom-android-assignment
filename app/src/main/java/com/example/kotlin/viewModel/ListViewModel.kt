package com.example.kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.repo.CarsRepository
import com.example.kotlin.util.Util.SKIP
import com.example.kotlin.util.Util.TAKE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val carRepository: CarsRepository) : ViewModel() {

    var carList: Flow<PagingData<ApiResponse>>? = null

    var take=TAKE
    var skip=SKIP
    fun getAllProducts() {
        Log.d("ListViewModel", "getAllProducts() called")
        val result = carRepository.getAllProducts(take,skip).cachedIn(viewModelScope)
        carList = result
        Log.d("ListViewModel", "getAllProducts() completed")

    }
}