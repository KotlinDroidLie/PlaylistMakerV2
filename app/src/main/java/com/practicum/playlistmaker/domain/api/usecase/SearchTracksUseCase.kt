package com.practicum.playlistmaker.domain.api.usecase

import com.practicum.playlistmaker.domain.api.TrackRepositoryResult

interface SearchTracksUseCase {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(result: TrackRepositoryResult)
    }
}