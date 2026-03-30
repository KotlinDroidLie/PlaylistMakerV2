package com.practicum.playlistmaker.features.settings.domain.api

import com.practicum.playlistmaker.features.settings.domain.model.SettingsModel

interface ISettingsUseCase {
    fun switchTheme(isChecked: Boolean)
    fun getSettings(): SettingsModel
    fun saveSettings(model: SettingsModel)
}