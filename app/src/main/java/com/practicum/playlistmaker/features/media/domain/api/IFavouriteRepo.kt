package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface IFavouriteRepo {
    suspend fun insertTrack(track: TrackModel)
    suspend fun removeTrack(trackId: Int)
    fun getTracks(): Flow<List<TrackModel>>
}