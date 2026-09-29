package com.volunteernews24.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.inboxDataStore by preferencesDataStore(name = "inbox_prefs")

data class InboxMessage(
    val title: String,
    val body: String,
    val url: String?,
    val timestamp: Long
)

class InboxManager(private val context: Context) {
    private val INBOX_KEY = stringPreferencesKey("inbox_messages")
    private val gson = Gson()

    val inboxMessages: Flow<List<InboxMessage>> = context.inboxDataStore.data.map { preferences ->
        val json = preferences[INBOX_KEY]
        if (json != null) {
            val type = object : TypeToken<List<InboxMessage>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    suspend fun addMessage(title: String, body: String, url: String?) {
        context.inboxDataStore.edit { preferences ->
            val currentJson = preferences[INBOX_KEY]
            val currentList: MutableList<InboxMessage> = if (currentJson != null) {
                val type = object : TypeToken<List<InboxMessage>>() {}.type
                gson.fromJson(currentJson, type)
            } else {
                mutableListOf()
            }
            
            // Add at the beginning
            currentList.add(0, InboxMessage(title, body, url, System.currentTimeMillis()))
            
            // Keep only last 50 messages
            if (currentList.size > 50) {
                currentList.removeAt(currentList.size - 1)
            }
            
            preferences[INBOX_KEY] = gson.toJson(currentList)
        }
    }

    suspend fun clearMessages() {
        context.inboxDataStore.edit { preferences ->
            preferences[INBOX_KEY] = gson.toJson(emptyList<InboxMessage>())
        }
    }
}
