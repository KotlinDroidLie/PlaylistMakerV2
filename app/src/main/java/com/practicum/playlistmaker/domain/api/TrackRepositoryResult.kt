package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.TrackModel

sealed interface TrackRepositoryResult {
    data class Success(val tracks: List<TrackModel>) : TrackRepositoryResult
    object NotFound : TrackRepositoryResult
    object NetworkError : TrackRepositoryResult
}