package com.practicum.playlistmaker.di.domain

import com.practicum.playlistmaker.di.data.externalNavigatorModule
import com.practicum.playlistmaker.features.media.domain.api.IAddTrackToPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteRepo
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteInteractor
import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IDeletePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IFormatPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistTracksUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.api.IRemoveTrackUseCase
import com.practicum.playlistmaker.features.media.domain.impl.AddTrackToPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.impl.FavouriteInteractor
import com.practicum.playlistmaker.features.media.domain.impl.CreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.impl.DeletePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.impl.FormatPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.impl.GetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.impl.GetPlaylistTracksUseCase
import com.practicum.playlistmaker.features.media.domain.impl.GetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.domain.impl.RemoveTrackUseCase
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
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingInteractor
import com.practicum.playlistmaker.features.sharing.domain.impl.SharingInteractor
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

    single<ISharingInteractor>{
        SharingInteractor(
            get<IExternalNavigator>(),
            get<IFormatTrackUseCase>(),
            androidContext(),
        )
    }

    single<IFavouriteInteractor>{
        FavouriteInteractor(get<IFavouriteRepo>())
    }

    single<ICreatePlaylistUseCase>{
        CreatePlaylistUseCase(get<IPlaylistRepo>())
    }

    single<IGetPlaylistsUseCase> {
        GetPlaylistsUseCase(get<IPlaylistRepo>())
    }

    single<IAddTrackToPlaylistUseCase> {
        AddTrackToPlaylistUseCase(get<IPlaylistRepo>())
    }

    single<IGetPlaylistByIdUseCase>{
        GetPlaylistByIdUseCase(get<IPlaylistRepo>())
    }

    single<IGetPlaylistTracksUseCase>{
        GetPlaylistTracksUseCase(get<IPlaylistRepo>())
    }
    single<IFormatPlaylistUseCase>{
        FormatPlaylistUseCase()
    }
    single<IRemoveTrackUseCase>{
        RemoveTrackUseCase(get<IPlaylistRepo>())
    }
    single<IDeletePlaylistUseCase>{
        DeletePlaylistUseCase(get<IPlaylistRepo>())
    }

}