package com.example.kotlin.dao

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.api.ServiceDetailInterface
import com.example.kotlin.service.ServiceBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UsersViewModel @Inject constructor(private val userDetailRepository: UserDetailRepository) :
    ViewModel() {


    var userData: MutableLiveData<User> = MutableLiveData()

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

//        val user = userDetailRepository.getUserInfo(id)
//        if (user.value != null) {
//            userData.postValue(user.value)
//        }
    }

//TODO API REPOSITORY

    //     fun fetchUserDataFromAPI(bundle: Bundle?) {
//        try {
//            val id: Int = bundle?.getString("id")?.toInt() ?: return
//
//            // API isteğini yap
//            val carsDetailApi =
//                ServiceBuilder.buildService().create(ServiceDetailInterface::class.java)
//            val response =
//                carsDetailApi.getView(id).execute()
//
//
//            // API'den gelen verileri Room veritabanına kaydet
//            if (response.isSuccessful) {
//                val user = response.body()?.userInfo
//                Log.e("USER INFO WITH USERVİEWMODEL FETCHUSERDATA", user.toString())
//                Log.e(
//                    "nanananaı",
//                    userDetailRepository.getUserInfo.value.toString()
//                )
//                if (user != null) {
//                    val userInfo = User(user.id, user.nameSurname, user.phoneFormatted, user.phone)
//                    Log.e(
//                        "USERSSSSINFO",
//                        userInfo.toString()
//                    )
//                    addUser(userInfo)
//
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("CATCH E DUSTU INSERT DEN SONRA",e.toString())
//            // Hata yönetimi yapılabilir
//            e.printStackTrace()
//        }
//    }

}