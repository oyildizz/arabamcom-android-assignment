package com.example.kotlin.api

import com.example.kotlin.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceInterface {

    //    @Headers ("Content-Type:application/json")
    @GET("listing?sort=1&sortDirection=0")
    suspend fun getAllProducts(
        @Query("take") take: Int,
        @Query("skip") skip: Int
    ): Response<List<ApiResponse>>
}