package com.practicum.playlistmaker

import com.practicum.playlistmaker.core.models.TrackModel

sealed interface TrackRepositoryResult {
    data class Success(val tracks: List<TrackModel>) : TrackRepositoryResult
    object NotFound : TrackRepositoryResult
    object NetworkError : TrackRepositoryResult
}