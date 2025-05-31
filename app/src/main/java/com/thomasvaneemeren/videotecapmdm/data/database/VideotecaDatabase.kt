package com.thomasvaneemeren.videotecapmdm.data.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.data.database.dao.UserFavoriteDao
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.data.entities.UserFavoriteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [MovieEntity::class, UserFavoriteEntity::class], version = 1)
abstract class VideotecaDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun userFavoriteDao(): UserFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: VideotecaDatabase? = null

        fun getInstance(context: Context): VideotecaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VideotecaDatabase::class.java,
                    "videoteca_shared.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Semilla de datos inicial
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let {
                                    val dao = it.movieDao()
                                    dao.insert(
                                        MovieEntity(
                                            title = "Matrix",
                                            genre = "SCI-FI",
                                            synopsis = "Simulación virtual",
                                            duration = 136,
                                            director = "Hermanas Wachowski"
                                        )
                                    )
                                    dao.insert(
                                        MovieEntity(
                                            title = "Gladiator",
                                            genre = "ACTION",
                                            synopsis = "Roma y venganza",
                                            duration = 155,
                                            director = "Ridley Scott"
                                        )
                                    )
                                    dao.insert(
                                        MovieEntity(
                                            title = "Amélie",
                                            genre = "ROMANCE",
                                            synopsis = "Una chica especial en París",
                                            duration = 122,
                                            director = "Jean-Pierre Jeunet"
                                        )
                                    )

                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
