package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

sealed interface CreatePlaylistState {
    data class Editing(override val playlist: PlaylistUiModel) : CreatePlaylistState, WithData
    object Creating : CreatePlaylistState
    data class Created(val title: String) : CreatePlaylistState
    data class Error(override val playlist: PlaylistUiModel,val resIdErrorMessage: Int): CreatePlaylistState, WithData
}

interface WithData{
    val playlist: PlaylistUiModel
}