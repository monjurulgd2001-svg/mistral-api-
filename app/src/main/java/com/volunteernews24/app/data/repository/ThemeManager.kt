package com.volunteernews24.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore(name = "theme_prefs")

enum class AppThemeColor {
    RED, GREEN, BLUE, ORANGE
}

class ThemeManager(private val context: Context) {
    private val THEME_KEY = stringPreferencesKey("theme_color")

    val themeColorFlow: Flow<AppThemeColor> = context.themeDataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: AppThemeColor.RED.name
        try {
            AppThemeColor.valueOf(themeName)
        } catch (e: Exception) {
            AppThemeColor.RED
        }
    }

    suspend fun setThemeColor(color: AppThemeColor) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_KEY] = color.name
        }
    }
}
