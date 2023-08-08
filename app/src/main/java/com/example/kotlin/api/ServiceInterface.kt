package com.example.kotlin.api

import com.example.kotlin.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceInterface {

    //    @Headers ("Content-Type:application/json")
    @GET("listing")
    suspend fun getAllProducts(
        @Query("take") take: Int,
        @Query("skip") skip: Int,
        @Query("sort") sort: Int,
        @Query("sortDirection") sortDirection: Int,
        @Query("minYear") minYear: Int,
        @Query("maxYear") maxYear: Int
    ): Response<List<ApiResponse>>
}