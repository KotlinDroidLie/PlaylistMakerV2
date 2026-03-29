package com.practicum.playlistmaker.core.di

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.core.data.impl.RetrofitNetworkClient
import com.practicum.playlistmaker.core.data.impl.SharedPrefStorageClient
import com.practicum.playlistmaker.data.repository.LocalHistoryTracksRepository
import com.practicum.playlistmaker.features.search.data.RemoteTrackRepository
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.features.search.domain.api.ITrackRepository
import com.practicum.playlistmaker.domain.api.usecase.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackDurationUseCase
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackYearUseCase
import com.practicum.playlistmaker.domain.api.usecase.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.LoadSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.features.settings.domain.api.ISwitchThemeUseCase
import com.practicum.playlistmaker.domain.impl.AddTrackToHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.ClearSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.FormatTrackDurationUseCaseImpl
import com.practicum.playlistmaker.domain.impl.FormatTrackYearUseCaseImpl
import com.practicum.playlistmaker.domain.impl.GetSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.LoadSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.SaveSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.features.settings.domain.impl.SwitchThemeUseCase
import com.practicum.playlistmaker.features.settings.data.ThemeRepository
import com.practicum.playlistmaker.features.settings.domain.api.IThemeRepository

object Creator {
    private var historyRepository: HistoryTracksRepository? = null
    private fun getThemeRepository(context: Context): IThemeRepository {
        return ThemeRepository(SharedPrefStorageClient<Boolean>(
            context = context,
            dataKey = "THEME",
            type = object : TypeToken<Boolean>() {}.type
        ))
    }
    fun getSwitchThemeUseCase(context: Context): ISwitchThemeUseCase {
        return SwitchThemeUseCase(getThemeRepository(context))
    }

    private fun getTrackRepository(): ITrackRepository {
        return RemoteTrackRepository(RetrofitNetworkClient())
    }

    fun getSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCaseImpl(getTrackRepository())
    }
    private fun getHistoryRepository(context: Context): HistoryTracksRepository {
        return historyRepository ?: LocalHistoryTracksRepository(context).also {
            historyRepository = it
        }
    }

    fun getAddTrackToHistoryUseCase(context: Context): AddTrackToHistoryUseCase {
        return AddTrackToHistoryUseCaseImpl(getHistoryRepository(context))
    }

    fun getClearSearchHistoryUseCase(context: Context): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCaseImpl(getHistoryRepository(context))
    }

    fun getSearchHistoryUseCase(context: Context): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCaseImpl(getHistoryRepository(context))
    }

    fun getLoadSearchHistoryUseCase(context: Context): LoadSearchHistoryUseCase {
        return LoadSearchHistoryUseCaseImpl(getHistoryRepository(context))
    }

    fun getSaveSearchHistoryUseCase(context: Context): SaveSearchHistoryUseCase {
        return SaveSearchHistoryUseCaseImpl(getHistoryRepository(context))
    }

    fun getFormatTrackYearUseCase(): FormatTrackYearUseCase {
        return FormatTrackYearUseCaseImpl()
    }

    fun getFormatTrackDurationUseCase(): FormatTrackDurationUseCase {
        return FormatTrackDurationUseCaseImpl()
    }

}