package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

sealed interface CreatePlaylistState {
    data class Editing(val playlist: PlaylistUiModel) : CreatePlaylistState
    object Creating : CreatePlaylistState
    data class Created(val title: String) : CreatePlaylistState
}