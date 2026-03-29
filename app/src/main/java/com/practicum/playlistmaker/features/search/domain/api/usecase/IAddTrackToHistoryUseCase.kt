package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.core.TrackModel

interface IAddTrackToHistoryUseCase {
     fun execute(track: TrackModel)
}