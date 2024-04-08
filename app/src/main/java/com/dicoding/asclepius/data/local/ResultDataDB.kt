package com.dicoding.asclepius.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.model.ResultData

@Database(entities = [ResultData::class], version = 1, exportSchema = false)
abstract class ResultDataDB: RoomDatabase() {

    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile
        var INSTANCE: ResultDataDB? = null

        @Synchronized
        fun getInstance(context: Context): ResultDataDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    ResultDataDB::class.java,
                    "game_db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as ResultDataDB
        }
    }
}