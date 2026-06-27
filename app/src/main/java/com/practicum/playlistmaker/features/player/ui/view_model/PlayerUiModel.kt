package com.practicum.playlistmaker.features.player.ui.view_model

data class PlayerUiModel(
    val id: Int,
    val trackName: String,
    val artistName: String,
    val albumName: String?,
    val releaseDate: String?,
    val genre: String,
    val country: String,
    val trackDuration: String,
    val trackImage: String,
    val audioPreviewUrl: String,
    val isFavourite: Boolean
)
