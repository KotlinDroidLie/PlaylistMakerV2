package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

interface IHistoryUseCase {
    suspend fun getHistory(): List<TrackModel>
    fun clearHistory()
    fun saveToHistory(track: TrackModel)
}