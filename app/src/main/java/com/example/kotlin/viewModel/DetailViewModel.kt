package com.example.kotlin.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.model.ApiDetailResponse
import com.example.kotlin.dao.User
import com.example.kotlin.repo.CarsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {
    private val carRepository=CarsRepository()
    private val _detailData=MutableLiveData<ApiDetailResponse>()
    private val detailDataError= MutableLiveData<Boolean>()
    private val detailDataLoading= MutableLiveData<Boolean>()
    val detailData: MutableLiveData<ApiDetailResponse> get() = _detailData


    private val _userInfo = MutableLiveData<User>()
    val userInfo: MutableLiveData<User> get() = _userInfo


//    fun insertUser(user: User) {
//        val userDao = CarsDatabase.getDatabase(appContext)?.userInfoDao()
//        userDao?.insert(user)
//    }
//
//    fun getUserInfoFromRoom() {
//        val userDao = CarsDatabase.getDatabase(appContext)?.userInfoDao()
//        val userInfoLiveData = userDao?.getUserInfo()
//        _userInfo.postValue(userInfoLiveData?.value?.firstOrNull())
//    }

    @SuppressLint("NullSafeMutableLiveData")
    suspend fun getView(id: Int) {
        try {
            detailDataLoading.postValue(true)

            val response = withContext(Dispatchers.IO) {
                carRepository.getProductDetail(id)
            }
            _detailData.postValue(response)
            detailDataError.postValue(false)
        } catch (e: Exception) {
            _detailData.postValue(null)
            detailDataError.postValue(true)
        } finally {
            detailDataLoading.postValue(false)
        }
    }
}