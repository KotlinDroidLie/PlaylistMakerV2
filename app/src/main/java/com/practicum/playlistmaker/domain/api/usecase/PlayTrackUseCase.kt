package com.practicum.playlistmaker.domain.api.usecase

import com.practicum.playlistmaker.domain.models.TrackModel

interface PlayTrackUseCase {
    fun execute(track: TrackModel)
}