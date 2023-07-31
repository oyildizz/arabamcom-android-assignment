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

    @Query("SELECT*FROM user ")
    fun getUserInfo(): LiveData<User>
}