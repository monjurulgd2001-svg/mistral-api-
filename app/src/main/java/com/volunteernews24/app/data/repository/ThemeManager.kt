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

enum class AppThemeMode {
    SYSTEM, LIGHT, DARK
}

class ThemeManager(private val context: Context) {
    private val THEME_KEY = stringPreferencesKey("theme_color")
    private val MODE_KEY = stringPreferencesKey("theme_mode")

    val themeColorFlow: Flow<AppThemeColor> = context.themeDataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: AppThemeColor.RED.name
        try {
            AppThemeColor.valueOf(themeName)
        } catch (e: Exception) {
            AppThemeColor.RED
        }
    }

    val themeModeFlow: Flow<AppThemeMode> = context.themeDataStore.data.map { preferences ->
        val modeName = preferences[MODE_KEY] ?: AppThemeMode.SYSTEM.name
        try {
            AppThemeMode.valueOf(modeName)
        } catch (e: Exception) {
            AppThemeMode.SYSTEM
        }
    }

    suspend fun setThemeColor(color: AppThemeColor) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_KEY] = color.name
        }
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.themeDataStore.edit { preferences ->
            preferences[MODE_KEY] = mode.name
        }
    }
}
