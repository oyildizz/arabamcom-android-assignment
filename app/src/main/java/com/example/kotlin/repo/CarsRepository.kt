package com.example.kotlin.repo


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.kotlin.api.ServiceDetailInterface
import com.example.kotlin.api.ServiceInterface
import com.example.kotlin.di.ServiceBuilder
import com.example.kotlin.model.ApiDetailResponse
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.paging.CarsPagingSource
import com.example.kotlin.util.Util.TAKE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarsRepository @Inject constructor() {
    private val carApi = ServiceBuilder.buildService().create(ServiceInterface::class.java)
    private val carDetailApi = ServiceBuilder.buildService().create(ServiceDetailInterface::class.java)

     fun getAllProducts(sort:Int,sortDirection:Int): Flow<PagingData<ApiResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = TAKE,
                enablePlaceholders = false // Don't show placeholders for items yet to be loaded
            ),
            pagingSourceFactory = { CarsPagingSource(carApi,sort,sortDirection) }
        ).flow
    }

//    suspend fun getAllProducts(): List<ApiResponse> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = carApi.getAllProducts().execute()
//                if (response.isSuccessful) {
//                    response.body() ?: emptyList()
//                } else {
//                    throw Exception("Error fetching products: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                throw Exception("Error fetching products: ${e.message}")
//            }
//        }
//    }

    suspend fun getProductDetail(id: Int): ApiDetailResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = carDetailApi.getView(id).execute()
                if (response.isSuccessful) {
                    response.body() ?: throw Exception("Response body is null in detail.")
                } else {
                    throw Exception("Error fetching detail data: ${response.message()}")
                }
            } catch (e: Exception) {
                throw Exception("Error fetching detail data: ${e.message}")
            }
        }
    }
}
