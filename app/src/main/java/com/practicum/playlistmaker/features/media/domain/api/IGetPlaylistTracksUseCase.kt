package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface IGetPlaylistTracksUseCase {
    operator fun invoke(tracksIds: List<Int>): Flow<List<TrackModel>>
}