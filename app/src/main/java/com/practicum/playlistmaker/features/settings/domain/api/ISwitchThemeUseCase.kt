package com.practicum.playlistmaker.features.settings.domain.api

interface ISwitchThemeUseCase {
    fun switchTheme(isChecked: Boolean)
    fun isDarkThemeEnable(): Boolean
}