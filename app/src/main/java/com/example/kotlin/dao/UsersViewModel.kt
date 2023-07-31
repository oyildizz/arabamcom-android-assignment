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

    init {
        loadRecords()
    }

    fun getRecordsObserver(): MutableLiveData<User> {
        return userData
    }

    fun addUser(usersData: User) {
        userDetailRepository.insert(usersData)
        loadRecords()

    }

    fun addAllUser(userList: List<User>) {
        userDetailRepository.addAllUsers(userList)
        loadRecords()
    }

    fun loadRecords() {
        val user = userDetailRepository.getUserInfo
        if (user.value != null) {
            userData.postValue(user.value)
        }
    }


    suspend fun fetchUserDataFromAPI(bundle: Bundle?) {
        try {
            val id: Int = bundle?.getString("id")?.toInt() ?: return

            // API isteğini yap
            val carsDetailApi =
                ServiceBuilder.buildService().create(ServiceDetailInterface::class.java)
            val response = withContext(Dispatchers.IO) {
                carsDetailApi.getView(id).execute()
            }

            // API'den gelen verileri Room veritabanına kaydet
            if (response.isSuccessful) {
                val user = response.body()?.userInfo
                Log.e("USER INFO WITH USERVİEWMODEL FETCHUSERDATA", user.toString())
                Log.e(
                    "nanananaı",
                    userDetailRepository.getUserInfo.value.toString()
                )
                if (user != null) {
                    val userInfo = User(user.id, user.nameSurname, user.phoneFormatted, user.phone)
                    Log.e(
                        "USERSSSSINFO",
                        userInfo.toString()
                    )
                    addUser(userInfo)

                }
            }
        } catch (e: Exception) {
            // Hata yönetimi yapılabilir
            e.printStackTrace()
        }
    }

}