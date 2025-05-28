package com.thomasvaneemeren.videotecapmdm.data.database

import android.content.Context
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseFactory @Inject constructor(
    private val context: Context
) {
    fun createDatabase(userName: String): VideotecaDatabase {
        val db = VideotecaDatabase.getInstance(context, userName)

        // Precargar películas si no existen
        runBlocking {
            val movieDao = db.movieDao()
            val movies = movieDao.getMoviesByUser(userName).first()
            if (movies.isEmpty()) {
                movieDao.insert(
                    MovieEntity(title = "Matrix", genre = "SCI-FI", synopsis = "Simulación virtual", duration = 136, director = "Hermanas Wachowski", userId = userName)
                )
                movieDao.insert(
                    MovieEntity(title = "Gladiator", genre = "ACTION", synopsis = "Roma y venganza", duration = 155, director = "Ridley Scott", userId = userName)
                )
                movieDao.insert(
                    MovieEntity(title = "Amélie", genre = "ROMANCE", synopsis = "Una chica especial en París", duration = 122, director = "Jean-Pierre Jeunet", userId = userName)
                )
            }
        }

        return db
    }
}
