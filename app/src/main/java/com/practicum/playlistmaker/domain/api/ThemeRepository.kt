package com.practicum.playlistmaker.domain.api

interface ThemeRepository {
    fun isDarkThemeEnable(): Boolean
    fun switchTheme(isChecked: Boolean)
}