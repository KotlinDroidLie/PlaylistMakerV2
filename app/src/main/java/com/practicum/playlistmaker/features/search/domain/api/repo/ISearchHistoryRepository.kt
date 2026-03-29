package com.practicum.playlistmaker.features.search.domain.api.repo

import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.core.data.dto.Resource

interface ISearchHistoryRepository {
    fun saveToHistory(m: TrackModel)
    fun getHistory(): Resource<List<TrackModel>>
    fun clearHistory()
}