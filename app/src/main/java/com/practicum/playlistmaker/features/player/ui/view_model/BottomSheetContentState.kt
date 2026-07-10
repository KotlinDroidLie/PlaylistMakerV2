package com.practicum.playlistmaker.features.player.ui.view_model

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

sealed interface BottomSheetContentState {
    data class Content(val playlists: List<PlaylistModel>): BottomSheetContentState
    data class SuccessAdded(val title: String): BottomSheetContentState
    data class AlreadyExists(val title: String) : BottomSheetContentState
    data class Error(val resMessage: Int): BottomSheetContentState
}