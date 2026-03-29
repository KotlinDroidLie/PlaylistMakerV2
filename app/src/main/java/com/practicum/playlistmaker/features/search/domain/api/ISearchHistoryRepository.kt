package com.practicum.playlistmaker.features.search.domain.api


import com.practicum.playlistmaker.core.data.dto.Resource
import com.practicum.playlistmaker.domain.models.TrackModel

interface ISearchHistoryRepository {
    fun saveToHistory(m: TrackModel)
    fun getHistory(): Resource<List<TrackModel>>
    fun clearHistory()
}