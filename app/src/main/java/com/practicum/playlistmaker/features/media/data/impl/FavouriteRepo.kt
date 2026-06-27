package com.practicum.playlistmaker.features.media.data.impl

import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.media.data.db.entity.toEntity
import com.practicum.playlistmaker.features.media.data.db.entity.toModel
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteRepo
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteRepo(
    private val appDataBase: AppDataBase
): IFavouriteRepo {
    override suspend fun insertTrack(track: TrackModel) {
        val entity = track.toEntity()
        appDataBase.trackDao().insertTrack(entity)
    }

    override suspend fun removeTrack(trackId: Int) {
        appDataBase.trackDao().removeTrack(trackId)
    }

    override fun getTracks(): Flow<List<TrackModel>> {
        return appDataBase.trackDao()
            .getFavouriteTracks()
            .map {entities -> entities.map { it.toModel() }}
    }
}