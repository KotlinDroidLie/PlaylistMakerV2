package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase

class App : Application() {
    private lateinit var settingsUseCase: ISettingsUseCase
    override fun onCreate() {
        super.onCreate()
        settingsUseCase = Creator.getSettingsUseCase(this)
        val settings = settingsUseCase.getSettings()
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkThemeEnable) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}