package com.practicum.playlistmaker.features.search.data.impl

import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.search.data.api.StorageClient
import com.practicum.playlistmaker.features.search.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.features.search.data.extensions.toDomain
import com.practicum.playlistmaker.features.search.data.extensions.toDto
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.model.TrackModel

class SearchHistoryRepository(
    private val storage: StorageClient<MutableList<TrackHistoryDto>>,
    private val appDataBase: AppDataBase
): ISearchHistoryRepository {

    override fun saveToHistory(m: TrackModel) {
        val dto = m.toDto()
        val trackHistory = storage.getData() ?: mutableListOf()
        val removeId = trackHistory.indexOfFirst{ it.trackId == dto.trackId }
        if (removeId != -1){
            trackHistory.removeAt(removeId)
        } else if(trackHistory.size == MAX_SIZE){
            trackHistory.removeAt(MAX_SIZE-1)
        }
        trackHistory.add(0, dto)
        storage.storeData(trackHistory)
    }

    override suspend fun getHistory(): List<TrackModel> {
        val tracksDto = storage.getData() ?: listOf()
        val tracks = tracksDto.map { it.toDomain() }
        val favouriteTrackIds = appDataBase.trackDao().getFavouriteTracksIds()
        tracks.forEach { track ->
            track.isFavourite = track.trackId in favouriteTrackIds
        }
        return tracks
    }

    override fun clearHistory() {
        storage.storeData(mutableListOf())
    }
    companion object{
        private const val MAX_SIZE = 10
    }
}