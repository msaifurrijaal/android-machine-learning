package com.dicoding.asclepius.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "result")
data class ResultData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val image: String,
    val result: String,
    val date: String
)
