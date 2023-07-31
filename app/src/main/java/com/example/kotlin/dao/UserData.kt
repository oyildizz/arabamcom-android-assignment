package com.example.kotlin.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name_surname")
    val nameSurname: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "phone_formatted")
    val phoneFormatted: String
)