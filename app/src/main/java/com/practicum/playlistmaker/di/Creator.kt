package com.practicum.playlistmaker.di

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.LocalHistoryTracksRepository
import com.practicum.playlistmaker.data.repository.LocalThemeRepositoryImpl
import com.practicum.playlistmaker.data.repository.RemoteTrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.api.repo.ThemeRepository
import com.practicum.playlistmaker.domain.api.repo.TrackRepository
import com.practicum.playlistmaker.domain.api.usecase.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.api.usecase.ShareAppUseCase
import com.practicum.playlistmaker.domain.api.usecase.SwitchThemeUseCase
import com.practicum.playlistmaker.domain.api.usecase.UserAgreementUseCase
import com.practicum.playlistmaker.domain.api.usecase.WriteSupportUseCase
import com.practicum.playlistmaker.domain.impl.AddTrackToHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.ClearSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.GetSearchHistoryUseCaseImpl
import com.practicum.playlistmaker.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.domain.impl.ShareAppUseCaseImpl
import com.practicum.playlistmaker.domain.impl.SwitchThemeUseCaseImpl
import com.practicum.playlistmaker.domain.impl.UserAgreementUseCaseImpl
import com.practicum.playlistmaker.domain.impl.WriteSupportUseCaseImpl

object Creator {
    private var historyRepository: HistoryTracksRepository? = null
    private fun getThemeRepository(context: Context): ThemeRepository {
        return LocalThemeRepositoryImpl(context)
    }
    fun getSwitchThemeUseCase(context: Context): SwitchThemeUseCase {
        return SwitchThemeUseCaseImpl(getThemeRepository(context))
    }

    private fun getTrackRepository(): TrackRepository {
        return RemoteTrackRepositoryImpl(RetrofitNetworkClient())
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

    fun getShareAppUseCase(context :Context): ShareAppUseCase{
        return ShareAppUseCaseImpl(context)
    }

    fun getWriteSupportUseCase(context: Context): WriteSupportUseCase{
        return WriteSupportUseCaseImpl(context)
    }

    fun getUserAgreementUseCase(context: Context): UserAgreementUseCase{
        return UserAgreementUseCaseImpl(context)
    }


}