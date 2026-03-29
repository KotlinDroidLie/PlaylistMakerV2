package com.practicum.playlistmaker.core.di

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.core.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.core.data.impl.RetrofitNetworkClient
import com.practicum.playlistmaker.core.data.impl.SharedPrefStorageClient
import com.practicum.playlistmaker.features.search.data.RemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IAddTrackToHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.IClearSearchHistoryUseCase
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackDurationUseCase
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackYearUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.IGetSearchHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.settings.domain.api.ISwitchThemeUseCase
import com.practicum.playlistmaker.features.search.domain.impl.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.impl.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.features.player.domain.impl.FormatTrackDurationUseCase
import com.practicum.playlistmaker.features.player.domain.impl.FormatTrackYearUseCase
import com.practicum.playlistmaker.features.search.domain.impl.GetSearchHistoryUseCase
import com.practicum.playlistmaker.features.search.data.SearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.impl.SearchTracksUseCase
import com.practicum.playlistmaker.features.settings.domain.impl.SwitchThemeUseCase
import com.practicum.playlistmaker.features.settings.data.ThemeRepository
import com.practicum.playlistmaker.features.settings.domain.api.IThemeRepository

object Creator {
    private const val HISTORY_KEY = "HISTORY_KEY"
    private const val THEME_KEY = "THEME_KEY"
    private fun getThemeRepository(context: Context): IThemeRepository {
        return ThemeRepository(SharedPrefStorageClient<Boolean>(
            context = context,
            dataKey = THEME_KEY,
            type = object : TypeToken<Boolean>() {}.type
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

    fun getAddTrackToHistoryUseCase(context: Context): IAddTrackToHistoryUseCase {
        return AddTrackToHistoryUseCase(getSearchHistoryRepository(context))
    }

    fun getClearSearchHistoryUseCase(context: Context): IClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCase(getSearchHistoryRepository(context))
    }

    fun getSearchHistoryUseCase(context: Context): IGetSearchHistoryUseCase {
        return GetSearchHistoryUseCase(getSearchHistoryRepository(context))
    }

    fun getSearchTracksUseCase(context: Context): ISearchTracksUseCase {
        return SearchTracksUseCase(getTrackRepository(context))
    }

    fun getSwitchThemeUseCase(context: Context): ISwitchThemeUseCase {
        return SwitchThemeUseCase(getThemeRepository(context))
    }

//    fun getLoadSearchHistoryUseCase(context: Context): LoadSearchHistoryUseCase {
//        return LoadSearchHistoryUseCaseImpl(getSearchHistoryRepository(context))
//    }
//
//    fun getSaveSearchHistoryUseCase(context: Context): SaveSearchHistoryUseCase {
//        return SaveSearchHistoryUseCaseImpl(getSearchHistoryRepository(context))
//    }

    fun getFormatTrackYearUseCase(): IFormatTrackYearUseCase {
        return FormatTrackYearUseCase()
    }

    fun getFormatTrackDurationUseCase(): IFormatTrackDurationUseCase {
        return FormatTrackDurationUseCase()
    }
}