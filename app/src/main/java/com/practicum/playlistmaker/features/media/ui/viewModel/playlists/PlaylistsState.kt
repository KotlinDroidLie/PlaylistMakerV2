package com.practicum.playlistmaker.features.media.ui.viewModel.playlists

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

sealed interface PlaylistsState {
    data class Content(val playlists: List<PlaylistModel>): PlaylistsState
    object Empty: PlaylistsState
}