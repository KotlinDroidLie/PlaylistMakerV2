package com.practicum.playlistmaker.domain.api.repo

import com.practicum.playlistmaker.domain.api.TrackRepositoryResult
import com.practicum.playlistmaker.domain.models.TrackModel

interface TrackRepository {
    fun doRequest(expression: String): TrackRepositoryResult
}