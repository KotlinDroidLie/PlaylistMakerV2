package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.presentation.viewModelModule
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    private val settingsUseCase: ISettingsUseCase by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(viewModelModule)
        }
        val settings = settingsUseCase.getSettings()
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkThemeEnable) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}