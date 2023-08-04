package com.example.kotlin.dao

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val userDetailRepository: UserDetailRepository) :
    ViewModel() {

    private var userData: MutableLiveData<User> = MutableLiveData()

//    init {
//        loadRecords()
//    }

    fun getRecordsObserver(): MutableLiveData<User> {
        return userData
    }

//    fun getUser(id:Int):User?{
//        val user = userDetailRepository.getUserInfo(id)
//        return user
//    }
    fun addUser(usersData: User) {
        userDetailRepository.insert(usersData)
        loadRecords(usersData.id)
    }

    fun addAllUser(userList: List<User>) {
        userDetailRepository.addAllUsers(userList)
//        loadRecords()
    }

    fun loadRecords(id:Int) {
        val user = userDetailRepository.getUserInfo(id)
            userData.postValue(user)
    }

}