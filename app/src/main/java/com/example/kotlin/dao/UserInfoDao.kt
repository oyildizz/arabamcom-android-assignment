package com.example.kotlin.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserInfoDAO {
    @Insert
    fun insert(user: User)

    @Insert
    fun addAllUsers(objects: List<User>)

    @Query("select * from user where id= :id")
    fun getUserInfo(id:Int): User
}