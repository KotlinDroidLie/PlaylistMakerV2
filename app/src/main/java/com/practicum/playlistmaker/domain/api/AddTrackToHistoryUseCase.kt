package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.TrackModel

interface AddTrackToHistoryUseCase {
    operator fun invoke(track: TrackModel)
}