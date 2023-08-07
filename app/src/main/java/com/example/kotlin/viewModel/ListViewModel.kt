package com.example.kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.repo.CarsRepository
import com.example.kotlin.util.Util.SORT
import com.example.kotlin.util.Util.SORT_DIRECTION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val carRepository: CarsRepository) : ViewModel() {

    var carList: Flow<PagingData<ApiResponse>>? = null

    var sort=SORT
    var sortDirection=SORT_DIRECTION
    fun getAllProducts() {
        Log.d("ListViewModel", "getAllProducts() called")
        val data = carRepository.getAllProducts(sort,sortDirection).cachedIn(viewModelScope)
        carList = data
        Log.d("ListViewModel", "getAllProducts() completed")

    }
}