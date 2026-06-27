package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IFavouriteRepo
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteUseCase
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteUseCase(
    private val repo: IFavouriteRepo
): IFavouriteUseCase {
    override suspend fun insertTrack(track: TrackModel) {
        repo.insertTrack(track)
    }

    override suspend fun removeTrack(trackId: Int) {
        repo.removeTrack(trackId)
    }

    override fun getTracks(): Flow<List<TrackModel>> {
        return repo.getTracks().map { tracks -> tracks.reversed() }
    }
}