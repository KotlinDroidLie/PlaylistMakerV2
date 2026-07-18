package com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

sealed interface PlaylistDetailMenuState {
    data class Content(val playlist: PlaylistModel): PlaylistDetailMenuState
    object DeleteSuccess : PlaylistDetailMenuState
    data class DeleteError(val resMessage: Int) : PlaylistDetailMenuState
}

