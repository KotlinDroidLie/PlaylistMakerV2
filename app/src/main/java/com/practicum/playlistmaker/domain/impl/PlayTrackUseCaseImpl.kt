package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.usecase.PlayTrackUseCase
import com.practicum.playlistmaker.domain.models.TrackModel

class PlayTrackUseCaseImpl(private val mediaPlayer: MediaPlayer) : PlayTrackUseCase {
    override fun execute(track: TrackModel) {
        mediaPlayer.setDataSource(track.audioPreviewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
    }
}