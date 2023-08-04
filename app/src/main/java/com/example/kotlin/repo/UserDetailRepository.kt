package com.example.kotlin.repo

import com.example.kotlin.dao.User
import com.example.kotlin.dao.UserInfoDAO
import javax.inject.Inject


class UserDetailRepository @Inject constructor(private val userDao: UserInfoDAO) {

    fun getUserInfo(id:Int) : User = userDao.getUserInfo(id)

    fun insert(userData: User){
        userDao.insert(userData)
    }

    fun addAllUsers(userList:List<User>){
        userDao.addAllUsers(userList)
    }
}