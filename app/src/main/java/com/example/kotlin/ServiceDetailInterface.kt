package com.example.kotlin

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceDetailInterface {
    @GET("detail")
    fun getView(@Query("id") id: Int): Call<ApiDetailResponse>
}