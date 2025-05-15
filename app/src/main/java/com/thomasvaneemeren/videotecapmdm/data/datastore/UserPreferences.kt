package com.thomasvaneemeren.videotecapmdm.data.datastore

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class UserPreferences(
    val username: String = "",
    val isOnboardingCompleted: Boolean = false
)