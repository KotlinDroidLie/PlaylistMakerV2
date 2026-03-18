package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.data.repository.LocalThemeRepositoryImpl
import com.practicum.playlistmaker.domain.api.ThemeRepository

class App : Application() {
    private lateinit var themeRepository: ThemeRepository
    override fun onCreate() {
        super.onCreate()
        themeRepository = LocalThemeRepositoryImpl(this)
        themeRepository.switchTheme()
    }
}