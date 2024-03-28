package com.application.gosporttest.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update



@Dao
interface GoSportDao {
    @Insert
    suspend fun insert(dataGoSport: GoSport)

    @Insert
    suspend fun insertAll(dataGoSportList: List<GoSport>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGoSportWithConflict(goSport: GoSport)

    @Query("SELECT * FROM data_go_sport_table")
    suspend fun getAllDataGoSport(): List<GoSport>

    @Update
    suspend fun updateDataList(dataList: List<GoSport>)
}