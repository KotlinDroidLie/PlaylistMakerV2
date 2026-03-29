package com.practicum.playlistmaker.features.settings.domain.impl

import com.practicum.playlistmaker.features.settings.domain.api.ISwitchThemeUseCase
import com.practicum.playlistmaker.features.settings.domain.api.IThemeRepository

class SwitchThemeUseCase(private val themeRepository: IThemeRepository): ISwitchThemeUseCase {
    override fun switchTheme(isChecked: Boolean) {
        themeRepository.switchTheme(isChecked)
    }
    override fun isDarkThemeEnable(): Boolean {
        return themeRepository.isDarkThemeEnable()
    }
}