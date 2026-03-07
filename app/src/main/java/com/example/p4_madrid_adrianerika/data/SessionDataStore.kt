package com.example.p4_madrid_adrianerika.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "session")

class SessionDataStore(private val context: Context) {

    private val USER_ID_KEY = stringPreferencesKey("user_id")

    suspend fun saveUserId(id: String) {
        context.dataStore.edit { it[USER_ID_KEY] = id }
    }

    suspend fun getUserId(): String? {
        return context.dataStore.data.first()[USER_ID_KEY]
    }

    suspend fun clearUserId() {
        context.dataStore.edit { it.remove(USER_ID_KEY) }
    }
}