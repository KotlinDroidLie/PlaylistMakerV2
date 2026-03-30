package com.practicum.playlistmaker.features.settings.domain.model

data class SettingsModel(
    val isDarkThemeEnable: Boolean
){
    companion object{
        fun default() = SettingsModel(
            isDarkThemeEnable = false
        )
    }
}

