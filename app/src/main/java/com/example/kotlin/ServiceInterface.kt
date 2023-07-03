package com.example.kotlin

import  retrofit2.Call
import retrofit2.Response
import  retrofit2.http.GET
import  retrofit2.http.Headers

interface ServiceInterface {

//    @Headers ("Content-Type:application/json")
    @GET("listing?sort=1&sortDirection=0&take=10")
     fun getAllProducts(): Call<List<ApiResponse>>
}