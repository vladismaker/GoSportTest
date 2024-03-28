package com.application.gosporttest.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategoryWithConflict(category: Category)

    @Insert
    suspend fun insertAll(dataGoSportList: List<Category>)

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>
}