package com.thomasvaneemeren.videotecapmdm.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.database.dao.UserFavoriteDao
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.repository.MovieRepositoryImpl
import com.thomasvaneemeren.videotecapmdm.repository.MovieRepository
import com.thomasvaneemeren.videotecapmdm.repository.UserFavoriteRepository
import com.thomasvaneemeren.videotecapmdm.repository.UserFavoriteRepositoryImpl
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
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }

    @Provides
    @Singleton
    fun provideDatabaseFactory(@ApplicationContext context: Context): DatabaseFactory {
        return DatabaseFactory(context)
    }

    @Provides
    @Singleton
    fun provideUserFavoriteDao(databaseFactory: DatabaseFactory): UserFavoriteDao {
        val db = databaseFactory.createDatabase()
        return db.userFavoriteDao()
    }

    @Provides
    @Singleton
    fun provideUserFavoriteRepository(
        userFavoriteDao: UserFavoriteDao
    ): UserFavoriteRepository = UserFavoriteRepositoryImpl(userFavoriteDao)

    @Provides
    @Singleton
    fun provideMovieRepository(databaseFactory: DatabaseFactory): MovieRepository {
        val db = databaseFactory.createDatabase()
        return MovieRepositoryImpl(db.movieDao())
    }
}
