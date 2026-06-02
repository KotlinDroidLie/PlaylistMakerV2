package com.practicum.playlistmaker.features.search.domain.api.repo

import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.data.dto.Resource
import kotlinx.coroutines.flow.Flow

interface IRemoteTrackRepository {
    fun doRequest(expression: String): Flow<Resource<List<TrackModel>>>
}