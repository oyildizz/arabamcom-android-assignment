package com.example.kotlin.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kotlin.model.ApiResponse
import com.example.kotlin.api.ServiceInterface
import com.example.kotlin.util.Util.TAKE
import retrofit2.HttpException


class CarsPagingSource(private val serviceInterface: ServiceInterface, private val sort:Int,private val sortDirection:Int,private val minYear:Int,private val maxYear:Int): PagingSource<Int,ApiResponse >(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ApiResponse> {
        val skip = params.key?:0
        val take= TAKE

      return try {
           val response= serviceInterface.getAllProducts(take,skip,sort,sortDirection,minYear, maxYear).body()

           return LoadResult.Page(
               data = response!!,
               prevKey = if (skip == 0) null else skip - take,
               nextKey = if(response.isEmpty()) null else skip + take
           )
       } catch (e:Exception){
            LoadResult.Error(e)
       }catch(httpE: HttpException){
          LoadResult.Error(httpE)
       }
    }
    override fun getRefreshKey(state: PagingState<Int, ApiResponse>): Int?
    {
        return null
    }
}