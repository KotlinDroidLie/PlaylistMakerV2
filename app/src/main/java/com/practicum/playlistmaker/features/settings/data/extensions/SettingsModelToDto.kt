package com.practicum.playlistmaker.features.settings.data.extensions

import com.practicum.playlistmaker.features.settings.data.dto.SettingsDto
import com.practicum.playlistmaker.features.settings.domain.model.SettingsModel

fun SettingsModel.toDto(): SettingsDto{
    return SettingsDto(
        isDarkThemeEnable = isDarkThemeEnable
    )
}