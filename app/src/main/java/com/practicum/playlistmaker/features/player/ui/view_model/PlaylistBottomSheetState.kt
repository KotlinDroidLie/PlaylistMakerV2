package com.practicum.playlistmaker.features.player.ui.view_model

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

sealed interface PlaylistBottomSheetState {
    object Show : PlaylistBottomSheetState
    object Hide: PlaylistBottomSheetState
    data class Content(val playlists: List<PlaylistModel>): PlaylistBottomSheetState
    data class Added(val title: String): PlaylistBottomSheetState
    data class AlreadyExists(val title: String) : PlaylistBottomSheetState
    data class Error(val resMessage: Int): PlaylistBottomSheetState
}