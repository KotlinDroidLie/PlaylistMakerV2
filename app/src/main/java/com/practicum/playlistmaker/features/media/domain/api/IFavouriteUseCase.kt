package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface IFavouriteUseCase {
    suspend fun insertTrack(track: TrackModel)
    suspend fun removeTrack(track: TrackModel)
    fun getTracks(): Flow<List<TrackModel>>
}