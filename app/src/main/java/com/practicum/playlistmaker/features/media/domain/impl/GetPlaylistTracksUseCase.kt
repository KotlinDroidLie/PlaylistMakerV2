package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistTracksUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

class GetPlaylistTracksUseCase(
    private val repo: IPlaylistRepo
) : IGetPlaylistTracksUseCase {
    override fun invoke(tracksIds: List<Int>): Flow<List<TrackModel>> {
        return repo.getTracksByIds(tracksIds)
    }
}