package com.thomasvaneemeren.videotecapmdm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class VideotecaDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile private var INSTANCE: VideotecaDatabase? = null

        fun getInstance(context: Context, userId: String): VideotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    VideotecaDatabase::class.java,
                    "videoteca_$userId.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}