package com.practicum.playlistmaker.features.search.domain.api.repo

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

interface ISearchHistoryRepository {
    fun saveToHistory(m: TrackModel)
    fun getHistory(): List<TrackModel>
    fun clearHistory()
}