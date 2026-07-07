package com.practicum.playlistmaker.features.media.domain.model

data class PlaylistModel(
    val id: Int,
    val title: String,
    val description: String?,
    val uri: String?,
    val idsTracks: List<Int> = emptyList(),
    val totalTracks: Int
)