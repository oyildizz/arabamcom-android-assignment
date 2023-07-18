package com.example.kotlin.api

import com.example.kotlin.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface ServiceInterface {

//    @Headers ("Content-Type:application/json")
    @GET("listing?sort=1&sortDirection=0&take=10")
     fun getAllProducts(): Call<List<ApiResponse>>
}