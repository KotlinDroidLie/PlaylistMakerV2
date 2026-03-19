package com.practicum.playlistmaker.domain.api

interface SwitchThemeUseCase {
    fun switchTheme(isChecked: Boolean)
    fun isDarkThemeEnable(): Boolean
}