package com.application.gosporttest.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.application.gosporttest.App
import com.application.gosporttest.retrofit.ApiService
import com.application.gosporttest.room.Category
import com.application.gosporttest.room.DatabaseBuilder
import com.application.gosporttest.room.GoSport
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RepositoryImp:Repository {
    override suspend fun startRequestRetrofit(url: String): String? {
        return suspendCoroutine { continuation ->

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val call2 = apiService.getData(url)
            call2.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()?.string())
                    } else {
                        continuation.resume("error")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resume("error")
                }
            })
        }
    }

    override suspend fun checkInternet(): Boolean {
        return suspendCoroutine { continuation ->
            val connectivityManager = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNet = connectivityManager.activeNetwork
            if (activeNet != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNet)
                val resp = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                continuation.resume(resp)
            }else{
                continuation.resume(false)
            }
        }
    }

    override suspend fun convertDataCategories(data:String?):MutableList<Category> {
        val db = DatabaseBuilder.getInstance(App.context)
        val categoryDao = db.categoryDao()

        val list: MutableList<Category> = mutableListOf()
        val jObj = JSONObject(data.toString())
        val categoriesArray = jObj.getJSONArray("categories")
        for (i in 0 until categoriesArray.length()) {
            val categoryObject = categoriesArray.getJSONObject(i)
            list.add(Category(categoryObject.getString("idCategory").toInt(), categoryObject.getString("strCategory")))
            categoryDao.insertCategoryWithConflict(Category(categoryObject.getString("idCategory").toInt(), categoryObject.getString("strCategory")))
        }

        return list
    }

    override suspend fun convertDataFull(data:String?):MutableList<GoSport> {
        val db = DatabaseBuilder.getInstance(App.context)
        val goSportDao = db.goSportDao()
        val listFull: MutableList<GoSport> = mutableListOf()

        val jObj = JSONObject(data.toString())
        val mealsArray = jObj.getJSONArray("meals")
        for (i in 0 until mealsArray.length()) {
            val mealObject = mealsArray.getJSONObject(i)
            val idMeal = mealObject.getString("idMeal")
            val nameMeal = mealObject.getString("strMeal")
            val imageMeal = mealObject.getString("strMealThumb")
            val descriptionMeal = "${mealObject.getString("strIngredient1")}, ${mealObject.getString("strIngredient2")}, ${mealObject.getString("strIngredient3")}, ${mealObject.getString("strIngredient4")}, ${mealObject.getString("strIngredient5")}"
            val categoryMeal = mealObject.getString("strCategory")

            val goSport = GoSport(idMeal.toInt(), nameMeal, imageMeal, descriptionMeal, categoryMeal)
            listFull.add(goSport)
            goSportDao.insertGoSportWithConflict(goSport)
        }
        return listFull
    }

    override suspend fun filterDataFull(index:Int): MutableList<GoSport> {
        val db = DatabaseBuilder.getInstance(App.context)
        val goSportDao = db.goSportDao()
        val categoryDao = db.categoryDao()
        val listFull = goSportDao.getAllDataGoSport()
        val listCategory = categoryDao.getAllCategories()
        val listNewFull: MutableList<GoSport> = mutableListOf()
        for (i in listFull.indices) {
            if (listFull[i].category==listCategory[index].name){
                listNewFull.add(GoSport(listFull[i].id, listFull[i].name, listFull[i].image, listFull[i].description, listFull[i].category))
            }
        }

        return listNewFull
    }
}