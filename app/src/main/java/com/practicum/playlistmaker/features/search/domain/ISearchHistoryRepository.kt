package com.practicum.playlistmaker.features.search.domain

import com.bumptech.glide.load.engine.Resource
import com.practicum.playlistmaker.domain.models.TrackModel

interface ISearchHistoryRepository {
    fun saveToHistory(m: TrackModel)
    fun getHistory(): Resource<List<TrackModel>>
}