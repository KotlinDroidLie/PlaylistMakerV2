package com.practicum.playlistmaker.features.search.domain.api

import com.practicum.playlistmaker.core.data.Resource
import com.practicum.playlistmaker.domain.api.TrackRepositoryResult
import com.practicum.playlistmaker.domain.models.TrackModel

interface ITrackRepository {
    fun doRequest(expression: String): Resource<List<TrackModel>>
}