package com.practicum.playlistmaker.creator

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import com.practicum.playlistmaker.features.player.domain.impl.FormatTrackUseCase
import com.practicum.playlistmaker.features.search.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.features.search.data.impl.RemoteTrackRepository
import com.practicum.playlistmaker.features.search.data.impl.RetrofitNetworkClient
import com.practicum.playlistmaker.features.search.data.impl.SearchHistoryRepository
import com.practicum.playlistmaker.features.search.data.impl.SharedPrefStorageClient
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.search.domain.impl.HistoryUseCase
import com.practicum.playlistmaker.features.search.domain.impl.SearchTracksUseCase
import com.practicum.playlistmaker.features.settings.data.impl.SettingsRepository
import com.practicum.playlistmaker.features.settings.data.dto.SettingsDto
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsRepository
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase
import com.practicum.playlistmaker.features.settings.domain.impl.SettingsUseCase
import com.practicum.playlistmaker.features.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.features.sharing.domain.api.IExternalNavigator
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingUseCase
import com.practicum.playlistmaker.features.sharing.domain.impl.SharingUseCase

object Creator {
    private const val HISTORY_KEY = "HISTORY_KEY"
    private const val SETTINGS_KEY = "SETTINGS_KEY"
    private fun getSettingsRepository(context: Context): ISettingsRepository {
        return SettingsRepository(
            SharedPrefStorageClient<SettingsDto>(
                context = context,
                dataKey = SETTINGS_KEY,
                type = object : TypeToken<SettingsDto>() {}.type
            )
        )
    }
    private fun getSearchHistoryRepository(context: Context): ISearchHistoryRepository {
        return SearchHistoryRepository(
            SharedPrefStorageClient<MutableList<TrackHistoryDto>>(
                context = context,
                dataKey = HISTORY_KEY,
                type = object : TypeToken<MutableList<TrackHistoryDto>>() {}.type
            )
        )
    }
    private fun getTrackRepository(context: Context): IRemoteTrackRepository {
        return RemoteTrackRepository(RetrofitNetworkClient(context))
    }
    private fun getExternalNavigator(context: Context): IExternalNavigator {
        return ExternalNavigator(context)
    }
    fun getSharingUseCase(context: Context): ISharingUseCase {
        return SharingUseCase(getExternalNavigator(context), context)
    }

    fun getHistoryUseCase(context: Context): IHistoryUseCase {
        return HistoryUseCase(getSearchHistoryRepository(context))
    }

    fun getSearchTracksUseCase(context: Context): ISearchTracksUseCase {
        return SearchTracksUseCase(getTrackRepository(context))
    }

    fun getSettingsUseCase(context: Context): ISettingsUseCase {
        return SettingsUseCase(getSettingsRepository(context))
    }
    fun getFormatTrackUseCase(): IFormatTrackUseCase {
        return FormatTrackUseCase()
    }
}