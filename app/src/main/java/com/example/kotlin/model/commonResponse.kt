package com.example.kotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @Expose
    @SerializedName("cityName")
    val cityName: String,
    @Expose
    @SerializedName("townName")
    val townName: String
)

data class Category(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("name")
    val name: String
)

data class Property(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("value")
    val value: String
)