package com.application.gosporttest.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun getData(@Url fullUrl: String): Call<ResponseBody>
}