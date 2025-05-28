package com.thomasvaneemeren.videotecapmdm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.data.database.dao.UserFavoriteDao
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.data.entities.UserFavoriteEntity

@Database(entities = [MovieEntity::class, UserFavoriteEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class VideotecaDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun userFavoriteDao(): UserFavoriteDao


    companion object {
        fun getInstance(context: Context, userName: String): VideotecaDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                VideotecaDatabase::class.java,
                "videoteca_db" // base común para todos usuarios
            ).build()
        }

    }
}