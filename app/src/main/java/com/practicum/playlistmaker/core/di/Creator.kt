package com.practicum.playlistmaker.core.di

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.features.search.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.core.data.impl.RetrofitNetworkClient
import com.practicum.playlistmaker.core.data.impl.SharedPrefStorageClient
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import com.practicum.playlistmaker.features.player.domain.impl.FormatTrackUseCase
import com.practicum.playlistmaker.features.search.data.RemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase
import com.practicum.playlistmaker.features.search.data.SearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.impl.HistoryUseCase
import com.practicum.playlistmaker.features.search.domain.impl.SearchTracksUseCase
import com.practicum.playlistmaker.features.settings.domain.impl.SettingsUseCase
import com.practicum.playlistmaker.features.settings.data.SettingsRepository
import com.practicum.playlistmaker.features.settings.data.dto.SettingsDto
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsRepository

object Creator {
    private const val HISTORY_KEY = "HISTORY_KEY"
    private const val SETTINGS_KEY = "SETTINGS_KEY"
    private fun getSettingsRepository(context: Context): ISettingsRepository {
        return SettingsRepository(SharedPrefStorageClient<SettingsDto>(
            context = context,
            dataKey = SETTINGS_KEY,
            type = object : TypeToken<SettingsDto>() {}.type
        ))
    }
    private fun getSearchHistoryRepository(context: Context): ISearchHistoryRepository {
        return SearchHistoryRepository(SharedPrefStorageClient<MutableList<TrackHistoryDto>>(
            context = context,
            dataKey = HISTORY_KEY,
            type = object : TypeToken<MutableList<TrackHistoryDto>>() {}.type
        ))
    }
    private fun getTrackRepository(context: Context): IRemoteTrackRepository {
        return RemoteTrackRepository(RetrofitNetworkClient(context))
    }

    fun getHistoryUseCase(context: Context): IHistoryUseCase{
        return HistoryUseCase(getSearchHistoryRepository(context))
    }


    fun getSearchTracksUseCase(context: Context): ISearchTracksUseCase {
        return SearchTracksUseCase(getTrackRepository(context))
    }

    fun getSettingsUseCase(context: Context): ISettingsUseCase {
        return SettingsUseCase(getSettingsRepository(context))
    }

//    fun getLoadSearchHistoryUseCase(context: Context): LoadSearchHistoryUseCase {
//        return LoadSearchHistoryUseCaseImpl(getSearchHistoryRepository(context))
//    }
//
//    fun getSaveSearchHistoryUseCase(context: Context): SaveSearchHistoryUseCase {
//        return SaveSearchHistoryUseCaseImpl(getSearchHistoryRepository(context))
//    }
    fun getFormatTrackUseCase(): IFormatTrackUseCase{
        return FormatTrackUseCase()
    }
}