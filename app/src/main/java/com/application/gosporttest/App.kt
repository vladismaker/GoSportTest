package com.application.gosporttest

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.application.gosporttest.room.DatabaseBuilder

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        DatabaseBuilder.getInstance(context)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}