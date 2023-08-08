package com.example.kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.repo.CarsRepository
import com.example.kotlin.util.Util.MAX_YEAR
import com.example.kotlin.util.Util.MIN_YEAR
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
    var minYear=MIN_YEAR
    var maxYear= MAX_YEAR
    fun getAllProducts() {
        Log.d("ListViewModel", "getAllProducts() called")
        val data = carRepository.getAllProducts(sort,sortDirection,minYear, maxYear).cachedIn(viewModelScope)
        carList = data
        Log.d("ListViewModel", "getAllProducts() completed")

    }
}