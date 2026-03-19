package com.practicum.playlistmaker.domain.api.repo

interface ThemeRepository {
    fun isDarkThemeEnable(): Boolean
    fun switchTheme(isChecked: Boolean)
}