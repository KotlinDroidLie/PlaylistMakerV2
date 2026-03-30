package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.data.dto.ErrorType

interface ISearchTracksUseCase {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: List<TrackModel>?, errorMessage: String?, typeError: ErrorType?)
    }
}