package com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail

data class PlaylistUiModel(
    val id: Int,
    val title: String,
    val description: String?,
    val uri: String?,
    val totalTracks: Int,
    val totalDuration: Int
)
