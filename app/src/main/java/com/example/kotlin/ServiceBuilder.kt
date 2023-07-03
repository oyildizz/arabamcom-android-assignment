package com.example.kotlin

import  okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
//    private val client= OkHttpClient.Builder().build()

    //let's create an instance of this object
    fun buildService ():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://sandbox.arabamd.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}