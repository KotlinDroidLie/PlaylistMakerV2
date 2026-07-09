package com.practicum.playlistmaker.di.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.di.domain.useCaseModule
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteInteractor
import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.favourite.FavouriteTracksViewModel
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
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingUseCase
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
            get<ISharingUseCase>()
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

    viewModel {
        PlaylistBottomSheetViewModel(get<IGetPlaylistsUseCase>())
    }


}