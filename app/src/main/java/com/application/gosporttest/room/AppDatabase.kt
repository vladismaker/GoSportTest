package com.application.gosporttest.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GoSport::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goSportDao(): GoSportDao
    abstract fun categoryDao(): CategoryDao
}