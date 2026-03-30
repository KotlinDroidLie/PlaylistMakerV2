package com.practicum.playlistmaker.features.settings.data

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.core.data.api.StorageClient
import com.practicum.playlistmaker.features.settings.data.dto.SettingsDto
import com.practicum.playlistmaker.features.settings.data.extensions.toDomain
import com.practicum.playlistmaker.features.settings.data.extensions.toDto
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsRepository
import com.practicum.playlistmaker.features.settings.domain.model.SettingsModel

class SettingsRepository(private val storage: StorageClient<SettingsDto>): ISettingsRepository {
    override fun getSettings(): SettingsModel {
        val dto = storage.getData()
        return dto?.toDomain() ?: SettingsModel.default()
    }

    override fun switchTheme(isChecked: Boolean) {
        getSettings().copy(
            isDarkThemeEnable = isChecked
        ).apply {
            saveSettings(this)
        }
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun saveSettings(model: SettingsModel) {
        val dto = model.toDto()
        storage.storeData(dto)
    }

}