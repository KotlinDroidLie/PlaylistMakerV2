package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SwitchThemeUseCase
import com.practicum.playlistmaker.domain.api.ThemeRepository

class SwitchThemeUseCaseImpl(private val themeRepository: ThemeRepository): SwitchThemeUseCase {
    override fun switchTheme() {
        themeRepository.switchTheme()
    }
}