package com.thomasvaneemeren.videotecapmdm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class VideotecaDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        fun getInstance(context: Context, userName: String): VideotecaDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                VideotecaDatabase::class.java,
                "videoteca_db_$userName"
            ).build()
        }
    }
}


