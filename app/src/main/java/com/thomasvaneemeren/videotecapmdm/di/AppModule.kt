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
import com.thomasvaneemeren.videotecapmdm.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
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
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DatabaseFactory {
        return DatabaseFactory(context)
    }
}

class DatabaseFactory(private val context: Context) {

    private val instances = mutableMapOf<String, VideotecaDatabase>()

    fun getDatabase(username: String): VideotecaDatabase {
        return instances.getOrPut(username) {
            VideotecaDatabase.getInstance(context, "videoteca_db_$username")
        }
    }
}


