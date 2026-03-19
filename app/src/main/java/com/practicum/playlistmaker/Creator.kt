package com.practicum.playlistmaker

import android.content.Context
import com.practicum.playlistmaker.data.repository.LocalThemeRepositoryImpl
import com.practicum.playlistmaker.domain.api.SwitchThemeUseCase
import com.practicum.playlistmaker.domain.api.ThemeRepository
import com.practicum.playlistmaker.domain.impl.SwitchThemeUseCaseImpl

object Creator {
    private fun getThemeRepository(context: Context): ThemeRepository {
        return LocalThemeRepositoryImpl(context)
    }
    fun getSwitchThemeUseCase(context: Context): SwitchThemeUseCase{
        return SwitchThemeUseCaseImpl(getThemeRepository(context))
    }
}