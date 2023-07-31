package com.example.kotlin.service

import android.app.Application
import com.example.kotlin.dao.CarsDatabase
import com.example.kotlin.dao.UserInfoDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceBuilder {
//    private val client= OkHttpClient.Builder().build()


    @Provides
    @Singleton
    fun buildService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://sandbox.arabamd.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun getAppDB(context: Application): CarsDatabase {
        return CarsDatabase.getAppDB(context)
    }

    @Provides
    @Singleton
    fun getDao(appDB: CarsDatabase): UserInfoDAO {
        return appDB.getDao()
    }

}