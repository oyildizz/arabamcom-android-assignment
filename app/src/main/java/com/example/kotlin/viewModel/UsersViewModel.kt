package com.example.kotlin.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.dao.User
import com.example.kotlin.repo.UserDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val userDetailRepository: UserDetailRepository) :
    ViewModel() {

    private var userData: MutableLiveData<User> = MutableLiveData()

    fun getRecordsObserver(): MutableLiveData<User> {
        return userData
    }

    fun addUser(usersData: User) {
        userDetailRepository.insert(usersData)
        loadRecords(usersData.id)
    }

    fun loadRecords(id:Int) {
        val user = userDetailRepository.getUserInfo(id)
            userData.postValue(user)
    }

}