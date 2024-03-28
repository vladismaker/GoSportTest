package com.application.gosporttest.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_go_sport_table")
data class GoSport(
    @PrimaryKey val id:Int,
    var name: String,
    var image: String,
    var description: String,
    var category: String
)


