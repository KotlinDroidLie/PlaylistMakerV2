package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.core.models.TrackModel
import com.practicum.playlistmaker.core.data.dto.ErrorType

interface ISearchTracksUseCase {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: List<TrackModel>?, errorMessage: String?, typeError: ErrorType?)
    }
}