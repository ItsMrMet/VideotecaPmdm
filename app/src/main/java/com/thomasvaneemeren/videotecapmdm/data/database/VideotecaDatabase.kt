package com.thomasvaneemeren.videotecapmdm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class VideotecaDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: VideotecaDatabase? = null

        fun getInstance(context: Context, dbName: String): VideotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideotecaDatabase::class.java,
                    dbName
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
