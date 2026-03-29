package com.practicum.playlistmaker.domainTerminated.api.repoTerminated

interface ThemeRepository2 {
    fun isDarkThemeEnable(): Boolean
    fun switchTheme(isChecked: Boolean)
}