package com.practicum.playlistmaker.domainTerminated.api

import com.practicum.playlistmaker.core.TrackModel

sealed interface TrackRepositoryResult {
    data class Success(val tracks: List<TrackModel>) : TrackRepositoryResult
    object NotFound : TrackRepositoryResult
    object NetworkError : TrackRepositoryResult
}