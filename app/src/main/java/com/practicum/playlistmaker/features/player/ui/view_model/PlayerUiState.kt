package com.practicum.playlistmaker.features.player.ui.view_model

data class PlayerUiState(
    val track: PlayerUiModel,
    var playerState: PlayerState,
    var currentTime: String
)

enum class PlayerState {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}