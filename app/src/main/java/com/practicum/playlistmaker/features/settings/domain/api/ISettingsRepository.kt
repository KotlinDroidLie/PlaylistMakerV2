package com.practicum.playlistmaker.features.settings.domain.api

import com.practicum.playlistmaker.features.settings.domain.model.SettingsModel

interface ISettingsRepository {
    fun getSettings(): SettingsModel
    fun switchTheme(isChecked: Boolean)
}