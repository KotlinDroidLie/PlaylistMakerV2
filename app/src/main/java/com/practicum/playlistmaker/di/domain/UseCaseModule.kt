package com.practicum.playlistmaker.di.domain

import com.practicum.playlistmaker.di.data.externalNavigatorModule
import com.practicum.playlistmaker.features.media.domain.api.IAddPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteRepo
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteInteractor
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.impl.AddPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.impl.FavouriteInteractor
import com.practicum.playlistmaker.features.media.domain.impl.PlaylistInteractor
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import com.practicum.playlistmaker.features.player.domain.impl.FormatTrackUseCase
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.search.domain.impl.HistoryUseCase
import com.practicum.playlistmaker.features.search.domain.impl.SearchTracksUseCase
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsRepository
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase
import com.practicum.playlistmaker.features.settings.domain.impl.SettingsUseCase
import com.practicum.playlistmaker.features.sharing.domain.api.IExternalNavigator
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingUseCase
import com.practicum.playlistmaker.features.sharing.domain.impl.SharingUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val useCaseModule = module{
    includes(repositoryModule, externalNavigatorModule)

    single<IFormatTrackUseCase>{
        FormatTrackUseCase()
    }

    single<IHistoryUseCase>{
        HistoryUseCase(get<ISearchHistoryRepository>())
    }

    single<ISearchTracksUseCase>{
        SearchTracksUseCase(get<IRemoteTrackRepository>())
    }

    single<ISettingsUseCase>{
        SettingsUseCase(get<ISettingsRepository>())
    }

    single<ISharingUseCase>{
        SharingUseCase(get<IExternalNavigator>(), androidContext())
    }

    single<IFavouriteInteractor>{
        FavouriteInteractor(get<IFavouriteRepo>())
    }

    single<IPlaylistInteractor>{
        PlaylistInteractor(get<IPlaylistRepo>())
    }

    single<IAddPlaylistUseCase>{
        AddPlaylistUseCase(get<IPlaylistRepo>())
    }
}