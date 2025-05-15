package com.thomasvaneemeren.videotecapmdm.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.database.VideotecaDatabase
import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.data.repository.MovieRepository
import com.thomasvaneemeren.videotecapmdm.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    // Provee la base de datos basada en el nombre del usuario para multiusuario
    @Provides
    @Singleton
    suspend fun provideDatabase(
        @ApplicationContext context: Context,
        userPreferencesRepository: UserPreferencesRepository
    ): VideotecaDatabase {
        val username = userPreferencesRepository.getUsername() // Método suspend o blocking, ver nota abajo
        // Nota: para simplificar aquí, se podría hacer blocking o pasar username de otra forma
        return VideotecaDatabase.getInstance(context, username ?: "default_user")
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: VideotecaDatabase): MovieDao =
        database.movieDao()

    @Provides
    @Singleton
    fun provideMovieRepository(dao: MovieDao): MovieRepository =
        MovieRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>
    ): UserPreferencesRepository =
        UserPreferencesRepository(dataStore)
}
