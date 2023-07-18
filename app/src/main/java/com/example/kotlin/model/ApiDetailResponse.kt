package com.example.kotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiDetailResponse(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("location")
    val location: Location,
    @Expose
    @SerializedName("category")
    val category: Category,
    @Expose
    @SerializedName("modelName")
    val modelName: String,
    @Expose
    @SerializedName("price")
    val price: Int,
    @Expose
    @SerializedName("priceFormatted")
    val priceFormatted: String,
    @Expose
    @SerializedName("date")
    val date: String,
    @Expose
    @SerializedName("dateFormatted")
    val dateFormatted: String,
    @Expose
    @SerializedName("photos")
    val photos:List<String>,
    @Expose
    @SerializedName("properties")
    val properties: List<Property>,
    @Expose
    @SerializedName("text")
    val text:String,
    @Expose
    @SerializedName("userInfo")
    val userInfo: UserInfo
)

data class UserInfo(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("nameSurname")
    val nameSurname: String,
    @Expose
    @SerializedName("phone")
    val phone: String,
    @Expose
    @SerializedName("phoneFormatted")
    val phoneFormatted: String
)




