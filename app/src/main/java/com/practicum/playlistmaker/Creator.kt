package com.practicum.playlistmaker

import android.content.Context
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.LocalThemeRepositoryImpl
import com.practicum.playlistmaker.data.repository.RemoteTrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.domain.api.usecase.SwitchThemeUseCase
import com.practicum.playlistmaker.domain.api.repo.ThemeRepository
import com.practicum.playlistmaker.domain.api.repo.TrackRepository
import com.practicum.playlistmaker.domain.impl.SwitchThemeUseCaseImpl

object Creator {
    private fun getThemeRepository(context: Context): ThemeRepository {
        return LocalThemeRepositoryImpl(context)
    }
    fun getSwitchThemeUseCase(context: Context): SwitchThemeUseCase{
        return SwitchThemeUseCaseImpl(getThemeRepository(context))
    }

    private fun getTrackRepository(): TrackRepository {
        return RemoteTrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun getSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCaseImpl(getTrackRepository())
    }
}