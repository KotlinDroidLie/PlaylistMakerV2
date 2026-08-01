package com.practicum.playlistmaker.features.player.ui.activity

internal sealed interface PlaybackButtonViewState {
    object Play: PlaybackButtonViewState
    object Pause: PlaybackButtonViewState
}