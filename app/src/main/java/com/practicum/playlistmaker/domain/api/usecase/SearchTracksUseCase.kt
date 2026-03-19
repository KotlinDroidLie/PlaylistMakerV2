package com.practicum.playlistmaker.domain.api.usecase

import com.practicum.playlistmaker.domain.models.TrackModel

interface SearchTracksUseCase {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(tracks : List<TrackModel>)
    }
}