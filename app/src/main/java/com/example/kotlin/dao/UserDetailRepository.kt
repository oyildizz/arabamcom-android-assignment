package com.example.kotlin.dao

import androidx.lifecycle.LiveData
import javax.inject.Inject


class UserDetailRepository @Inject constructor(private val userDao: UserInfoDAO) {

    val getUserInfo: LiveData<User> = userDao.getUserInfo()

    fun insert(userData:User){
        userDao.insert(userData)
    }

    fun addAllUsers(userList:List<User>){
        userDao.addAllUsers(userList)
    }
}