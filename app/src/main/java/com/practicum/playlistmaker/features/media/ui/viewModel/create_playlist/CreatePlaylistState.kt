package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.PlaylistUiModel

sealed interface CreatePlaylistState {
    data class Editing(val playlist: PlaylistUiModel) : CreatePlaylistState
    data class Created(val playlist: PlaylistUiModel) : CreatePlaylistState
}