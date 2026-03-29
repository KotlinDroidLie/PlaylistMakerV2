package com.practicum.playlistmaker.features.settings.data

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.core.data.api.StorageClient
import com.practicum.playlistmaker.features.settings.domain.api.IThemeRepository

class ThemeRepository(private val storage: StorageClient<Boolean>): IThemeRepository {
    override fun isDarkThemeEnable(): Boolean {
        return storage.getData() ?: false
    }

    override fun switchTheme(isChecked: Boolean) {
        storage.storeData(isChecked)

        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}