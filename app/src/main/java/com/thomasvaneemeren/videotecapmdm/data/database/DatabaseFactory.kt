package com.thomasvaneemeren.videotecapmdm.data.database

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseFactory @Inject constructor(
    private val context: Context
) {
    fun createDatabase(): VideotecaDatabase {
        return VideotecaDatabase.getInstance(context)
    }
}
