package com.practicum.playlistmaker.domain.api.repo

interface ThemeRepository2 {
    fun isDarkThemeEnable(): Boolean
    fun switchTheme(isChecked: Boolean)
}