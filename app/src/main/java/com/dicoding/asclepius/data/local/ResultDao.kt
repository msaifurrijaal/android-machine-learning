package com.dicoding.asclepius.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.asclepius.data.model.ResultData

@Dao
interface ResultDao {
    @Insert
    fun insert(result: ResultData)

    @Query("SELECT * FROM result")
    fun getAll(): List<ResultData>
}