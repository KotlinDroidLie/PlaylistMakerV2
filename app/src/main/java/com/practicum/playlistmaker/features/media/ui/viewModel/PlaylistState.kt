package com.practicum.playlistmaker.features.media.ui.viewModel

import com.practicum.playlistmaker.features.media.domain.PlaylistModel

sealed interface PlaylistState {
    data class Content(val playlists: List<PlaylistModel>): PlaylistState
    object Empty: PlaylistState
}