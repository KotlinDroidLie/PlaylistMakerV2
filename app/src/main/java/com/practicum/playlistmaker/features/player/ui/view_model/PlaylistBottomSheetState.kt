package com.practicum.playlistmaker.features.player.ui.view_model

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

sealed interface PlaylistBottomSheetState {
    data class Content(val playlists: List<PlaylistModel>): PlaylistBottomSheetState
    object Hide: PlaylistBottomSheetState
    object Empty: PlaylistBottomSheetState
}