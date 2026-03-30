package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.core.models.TrackModel

interface IHistoryUseCase {
    fun getHistory(): List<TrackModel>
    fun clearHistory()
    fun saveToHistory(track: TrackModel)
}