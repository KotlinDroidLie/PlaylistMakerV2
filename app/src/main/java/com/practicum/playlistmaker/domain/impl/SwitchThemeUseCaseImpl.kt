package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.usecase.SwitchThemeUseCase
import com.practicum.playlistmaker.domain.api.repo.ThemeRepository

class SwitchThemeUseCaseImpl(private val themeRepository: ThemeRepository): SwitchThemeUseCase {
    override fun switchTheme(isChecked: Boolean) {
        themeRepository.switchTheme(isChecked)
    }
    override fun isDarkThemeEnable(): Boolean {
        return themeRepository.isDarkThemeEnable()
    }
}