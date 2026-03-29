package com.practicum.playlistmaker.features.search.domain.api

import com.practicum.playlistmaker.core.data.dto.Resource
import com.practicum.playlistmaker.domain.models.TrackModel

interface ITrackRepository {
    fun doRequest(expression: String): Resource<List<TrackModel>>
}