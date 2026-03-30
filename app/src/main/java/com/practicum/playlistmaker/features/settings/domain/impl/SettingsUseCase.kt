package com.practicum.playlistmaker.features.settings.domain.impl

import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsRepository
import com.practicum.playlistmaker.features.settings.domain.model.SettingsModel

class SettingsUseCase(private val settingsRepository: ISettingsRepository): ISettingsUseCase {
    override fun switchTheme(isChecked: Boolean) {
        settingsRepository.switchTheme(isChecked)
    }

    override fun getSettings(): SettingsModel {
        return settingsRepository.getSettings()
    }

    override fun saveSettings(model: SettingsModel) {
        settingsRepository.saveSettings(model)
    }

}