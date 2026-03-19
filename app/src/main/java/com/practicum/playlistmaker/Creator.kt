package com.practicum.playlistmaker

import android.content.Context
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.LocalHistoryTracksRepository
import com.practicum.playlistmaker.data.repository.LocalThemeRepositoryImpl
import com.practicum.playlistmaker.data.repository.RemoteTrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.api.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.domain.api.usecase.SwitchThemeUseCase
import com.practicum.playlistmaker.domain.api.repo.ThemeRepository
import com.practicum.playlistmaker.domain.api.repo.TrackRepository
import com.practicum.playlistmaker.domain.api.usecase.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.impl.AddTrackToHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.ClearSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.GetSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.SwitchThemeUseCaseImpl

object Creator {
    private var historyRepository: HistoryTracksRepository? = null
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
    private fun getHistoryRepository(context: Context): HistoryTracksRepository{
        return historyRepository ?: LocalHistoryTracksRepository(context).also {
            historyRepository = it
        }
    }

    fun getAddTrackToHistoryUseCase(context: Context): AddTrackToHistoryUseCase{
        return AddTrackToHistoryUseCaseImpl(getHistoryRepository(context))
    }

    fun getClearSearchHistoryUseCase(context: Context): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseImpl(getHistoryRepository(context))
    }

    fun getGetSearchHistoryUseCase(context: Context): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCaseImpl(getHistoryRepository(context))
    }


}