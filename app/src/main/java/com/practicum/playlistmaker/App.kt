package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.data.repository.LocalHistoryTracksRepository
import com.practicum.playlistmaker.data.repository.LocalThemeRepositoryImpl
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.api.repo.ThemeRepository

class App : Application() {
    private lateinit var historyTracksRepository: HistoryTracksRepository
    private lateinit var themeRepository: ThemeRepository
    override fun onCreate() {
        super.onCreate()
        historyTracksRepository = LocalHistoryTracksRepository(this)
        historyTracksRepository.loadHistory()
        themeRepository = LocalThemeRepositoryImpl(this)
        themeRepository.switchTheme(themeRepository.isDarkThemeEnable())
    }
}