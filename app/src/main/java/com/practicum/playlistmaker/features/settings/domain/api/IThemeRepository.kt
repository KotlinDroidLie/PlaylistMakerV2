package com.practicum.playlistmaker.features.settings.domain.api

interface IThemeRepository {
    fun isDarkThemeEnable(): Boolean
    fun switchTheme(isChecked: Boolean)
}