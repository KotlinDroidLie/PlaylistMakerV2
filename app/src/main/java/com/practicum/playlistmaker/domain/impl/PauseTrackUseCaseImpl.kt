package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.usecase.PauseTrackUseCase

class PauseTrackUseCaseImpl(private val mediaPlayer: MediaPlayer) : PauseTrackUseCase {
    override fun execute() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }
}