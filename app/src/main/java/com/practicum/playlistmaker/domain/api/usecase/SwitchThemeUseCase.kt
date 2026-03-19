package com.practicum.playlistmaker.domain.api.usecase

interface SwitchThemeUseCase {
    fun switchTheme(isChecked: Boolean)
    fun isDarkThemeEnable(): Boolean
}