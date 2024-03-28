package com.application.gosporttest.models

import com.application.gosporttest.room.Category
import com.application.gosporttest.room.GoSport

interface Repository {
    suspend fun startRequestRetrofit(url:String):String?
    suspend fun checkInternet():Boolean
    suspend fun convertDataCategories(data:String?):MutableList<Category>
    suspend fun convertDataFull(data:String?):MutableList<GoSport>
    suspend fun filterDataFull(index:Int):MutableList<GoSport>
}