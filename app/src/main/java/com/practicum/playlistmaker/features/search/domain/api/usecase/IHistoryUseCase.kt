package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.core.data.dto.Resource

interface IHistoryUseCase {
    fun getHistory(): Resource<List<TrackModel>>
    fun clearHistory()
    fun saveToHistory(track: TrackModel)
}