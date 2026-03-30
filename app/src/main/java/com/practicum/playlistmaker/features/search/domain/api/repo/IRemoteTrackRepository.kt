package com.practicum.playlistmaker.features.search.domain.api.repo

import com.practicum.playlistmaker.core.models.TrackModel
import com.practicum.playlistmaker.core.data.dto.Resource

interface IRemoteTrackRepository {
    fun doRequest(expression: String): Resource<List<TrackModel>>
}