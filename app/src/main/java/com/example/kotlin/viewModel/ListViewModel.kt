package com.example.kotlin.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.repo.CarsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private val carRepository = CarsRepository()
    private val _cars = MutableLiveData<List<ApiResponse>>()
    val cars: MutableLiveData<List<ApiResponse>> get() = _cars

    private val carsError = MutableLiveData<Boolean>()
    private val carsLoading = MutableLiveData<Boolean>()

    init {
        // ViewModel oluşturulduğunda verileri hemen alalım
        getAllProducts()
    }

    private fun getAllProducts() {
        carsLoading.postValue(true)
        Log.d("ListViewModel", "getAllProducts() called")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = carRepository.getAllProducts()
                _cars.postValue(data)
                carsError.postValue(false)
                Log.d("ListViewModel", "getAllProducts() success, data: $data")
            } catch (e: Exception) {
                _cars.postValue(emptyList())
                carsError.postValue(true)
                Log.e("ListViewModel", "getAllProducts() error: ${e.message}")
            } finally {
                carsLoading.postValue(false)
                Log.d("ListViewModel", "getAllProducts() completed")
            }
        }
    }
}














//package com.example.kotlin.viewModel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.kotlin.api.ServiceInterface
//import com.example.kotlin.model.ApiResponse
//import com.example.kotlin.service.ServiceBuilder
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class ListViewModel : ViewModel() {
//    private val _cars = MutableLiveData<List<ApiResponse>>()
//    val cars: MutableLiveData<List<ApiResponse>> get() = _cars
//
//    private val carsError = MutableLiveData<Boolean>()
//    private val carsLoading = MutableLiveData<Boolean>()
//
//    init {
//        // ViewModel oluşturulduğunda verileri hemen alalım
//        getAllProducts()
//    }
//
//    private fun getAllProducts() {
//        carsLoading.postValue(true) // Veri alımı başlamadan önce loading durumunu true olarak işaretle
//
//        // Kullanıcı tarafından veri güncellemelerini algılamak için coroutine kullanmayacağız
//        // LiveData otomatik olarak UI'da güncelleme sağlar
//        val carApi = ServiceBuilder.buildService().create(ServiceInterface::class.java)
//
//        // IO Dispatcher'da çalışırken verileri alalım
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = carApi.getAllProducts().execute()
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    if (data != null) {
//                        _cars.postValue(data)
//                        carsError.postValue(false) // Hata olmadığını işaretle
//                    } else {
//                        carsError.postValue(true) // Hata olduğunu işaretle
//                        throw Exception("Response body is null.")
//                    }
//                } else {
//                    carsError.postValue(true) // Hata olduğunu işaretle
//                    throw Exception("Error fetching products: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                carsError.postValue(true) // Hata olduğunu işaretle
//                throw Exception("Error fetching products: ${e.message}")
//            } finally {
//                carsLoading.postValue(false) // Veri alımı tamamlandığında loading durumunu false olarak işaretle
//            }
//        }
//    }
//}

































//package com.example.kotlin.viewModel
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.PagedList
//import com.example.kotlin.model.ApiResponse
//import com.example.kotlin.repo.CarsRepo
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//import io.reactivex.disposables.CompositeDisposable
//import io.reactivex.Scheduler
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//
//@HiltViewModel
//class ListViewModel : ViewModel() {
//
//    var carList  =  MutableLiveData<ApiResponse>()
//    var loadingList = MutableLiveData<Boolean>()
//    var errorList = MutableLiveData<Boolean>()
//
//    private val compositeDisposable = CompositeDisposable()
//
//    private val pageSize = 20
//
//    private lateinit var sourceFactory : CarDataSourceFactory
//    private lateinit var filterSourceFactory : FilterCarDataSourceFactory
//
//
//
//
//    fun loadList(sort:Int,sortDirection:Int){
//
//        sourceFactory = CarDataSourceFactory(compositeDisposable, ArabamApi.getService(),sort = sort,sortDirection = sortDirection)
//
//        val config = PagedList.Config.Builder()
//            .setPageSize(pageSize)
//            .setEnablePlaceholders(false)
//            .build()
//
//        val eventPagedList  = RxPagedListBuilder(sourceFactory,config)
//            .setFetchScheduler(Schedulers.io())
//            .buildObservable()
//            .cache()
//
//
//        compositeDisposable.add(eventPagedList.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe { loadingList.value = true }
//            .subscribe({
//                if (it.isNotEmpty()){
//                    carList.value = it
//                    loadingList.value = false
//                    errorList.value = false
//                }
//            }, {
//                errorList.value = true
//                loadingList.value = false
//            })
//        )
//    }
//}