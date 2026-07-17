package com.practicum.playlistmaker.di.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.di.domain.useCaseModule
import com.practicum.playlistmaker.features.media.domain.api.IAddTrackToPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteInteractor
import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IDeletePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IFormatPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistTracksUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.domain.api.IRemoveTrackUseCase
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.favourite.FavouriteTracksViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail.PlaylistDetailMenuViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail.PlaylistDetailViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.playlists.PlaylistViewModel
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import com.practicum.playlistmaker.features.player.ui.view_model.PlaylistBottomSheetViewModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.features.search.domain.api.usecase.IHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase
import com.practicum.playlistmaker.features.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingInteractor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    includes(useCaseModule)

    single<Handler>{
        Handler(Looper.getMainLooper())
    }

    factory<MediaPlayer>{
        MediaPlayer()
    }

    viewModel { (model: TrackModel) ->
        PlayerViewModel(
            model,
            get<IFormatTrackUseCase>(),
            get<MediaPlayer>(),
            get<IFavouriteInteractor>()
        )
    }

    viewModel{
        SearchViewModel(
            get<IHistoryUseCase>(),
            get<ISearchTracksUseCase>(),
            get<IFavouriteInteractor>()
        )
    }

    viewModel{
        SettingsViewModel(
            get<ISettingsUseCase>(),
            get<ISharingInteractor>()
        )
    }

    viewModel {
        FavouriteTracksViewModel(get<IFavouriteInteractor>())
    }

    viewModel {
        PlaylistViewModel(get<IGetPlaylistsUseCase>())
    }

    viewModel {
        CreatePlaylistViewModel(get<ICreatePlaylistUseCase>())
    }

    viewModel { (model: TrackModel) ->
        PlaylistBottomSheetViewModel(
            model,
            get<IGetPlaylistsUseCase>(),
            get<IAddTrackToPlaylistUseCase>()
        )
    }

    viewModel { (playlistId: Int) ->
        PlaylistDetailViewModel(
            removeTrackUseCase = get<IRemoveTrackUseCase>(),
            getPlaylistByIdUseCase = get<IGetPlaylistByIdUseCase>(),
            getPlaylistTracks = get<IGetPlaylistTracksUseCase>(),
            formatPlaylistUseCase = get<IFormatPlaylistUseCase>(),
            sharingInteractor = get<ISharingInteractor>(),
            playlistId = playlistId
        )
    }

    viewModel { (playlistId: Int) ->
        PlaylistDetailMenuViewModel(
            getPlaylistByIdUseCase = get<IGetPlaylistByIdUseCase>(),
            deletePlaylistUseCase = get<IDeletePlaylistUseCase>(),
            playlistId = playlistId
        )
    }


}