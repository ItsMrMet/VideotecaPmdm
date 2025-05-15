package com.thomasvaneemeren.videotecapmdm.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
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

    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

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

    // No proporcionamos VideotecaDatabase ni MovieDao aquí
}

