package com.practicum.playlistmaker.core.app

import android.app.Application
import com.practicum.playlistmaker.core.di.Creator
import com.practicum.playlistmaker.features.settings.domain.api.ISwitchThemeUseCase

class App : Application() {
    private lateinit var switchThemeUseCase: ISwitchThemeUseCase
    override fun onCreate() {
        super.onCreate()
        switchThemeUseCase = Creator.getSwitchThemeUseCase(this)
        switchThemeUseCase.switchTheme(switchThemeUseCase.isDarkThemeEnable())
    }
}