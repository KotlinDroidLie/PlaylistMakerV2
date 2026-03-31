package com.practicum.playlistmaker.features.player.ui.view_model

data class PlayerState(
    val track: PlayerUiModel,
    val timer: String,
    val playerStatus: Int
)