package com.practicum.playlistmaker.features.search.data

import com.practicum.playlistmaker.core.data.api.StorageClient
import com.practicum.playlistmaker.core.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.core.data.extensions.toDomain
import com.practicum.playlistmaker.core.data.extensions.toDto
import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository

class SearchHistoryRepository(private val storage: StorageClient<MutableList<TrackHistoryDto>>): ISearchHistoryRepository {

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

    override fun getHistory(): List<TrackModel> {
        val trackHistory = storage.getData() ?: listOf()
        val model = trackHistory.map { it.toDomain() }
        return model
    }

    override fun clearHistory() {
        storage.storeData(mutableListOf())
    }
    companion object{
        private const val MAX_SIZE = 10
    }
}
