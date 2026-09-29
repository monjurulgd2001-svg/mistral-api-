package com.volunteernews24.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.volunteernews24.app.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "bookmarks")

class BookmarkManager(private val context: Context) {
    private val gson = Gson()
    private val bookmarksKey = stringPreferencesKey("bookmarked_articles")

    val bookmarkedArticles: Flow<List<Article>> = context.dataStore.data
        .map { preferences ->
            val json = preferences[bookmarksKey] ?: "[]"
            val type = object : TypeToken<List<Article>>() {}.type
            gson.fromJson(json, type)
        }

    suspend fun toggleBookmark(article: Article): Boolean {
        var isNowBookmarked = false
        context.dataStore.edit { preferences ->
            val json = preferences[bookmarksKey] ?: "[]"
            val type = object : TypeToken<MutableList<Article>>() {}.type
            val bookmarks: MutableList<Article> = gson.fromJson(json, type)

            val existingIndex = bookmarks.indexOfFirst { it.url == article.url }
            if (existingIndex >= 0) {
                bookmarks.removeAt(existingIndex)
                isNowBookmarked = false
            } else {
                bookmarks.add(article.copy(isBookmarked = true))
                isNowBookmarked = true
            }
            preferences[bookmarksKey] = gson.toJson(bookmarks)
        }
        return isNowBookmarked
    }
}
