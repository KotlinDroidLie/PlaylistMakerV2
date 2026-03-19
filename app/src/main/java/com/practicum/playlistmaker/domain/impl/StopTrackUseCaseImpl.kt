package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.usecase.StopTrackUseCase

class StopTrackUseCaseImpl(private val mediaPlayer: MediaPlayer) : StopTrackUseCase {
    override fun execute() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }
}