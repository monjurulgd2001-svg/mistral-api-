package com.volunteernews24.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.volunteernews24.app.data.repository.AppThemeColor
import com.volunteernews24.app.data.repository.AppThemeMode
import com.volunteernews24.app.data.repository.ThemeManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themeManager = ThemeManager(application)

    val currentTheme: StateFlow<AppThemeColor> = themeManager.themeColorFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppThemeColor.RED
    )

    val currentMode: StateFlow<AppThemeMode> = themeManager.themeModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppThemeMode.SYSTEM
    )

    fun setTheme(color: AppThemeColor) {
        viewModelScope.launch {
            themeManager.setThemeColor(color)
        }
    }

    fun setMode(mode: AppThemeMode) {
        viewModelScope.launch {
            themeManager.setThemeMode(mode)
        }
    }
}
