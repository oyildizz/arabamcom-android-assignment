package com.example.kotlin.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//TODO bağımliliklari ögren
//@Module
//@InstallIn(SingletonComponent::class)
object ServiceBuilder {
//    private val client= OkHttpClient.Builder().build()


//    @Provides
//    @Singleton
    fun buildService ():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://sandbox.arabamd.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}