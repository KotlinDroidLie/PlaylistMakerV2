package com.practicum.playlistmaker.domain.api.repo

import com.practicum.playlistmaker.domain.models.TrackModel

interface HistoryTracksRepository {
    fun getHistory(): MutableList<TrackModel>
    fun addTrack(track: TrackModel)
    fun clearHistory()
    fun saveHistory()
    fun loadHistory()
}