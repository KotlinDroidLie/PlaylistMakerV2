package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.TrackModel

interface HistoryTracksRepository {
    fun getHistory(): List<TrackModel>
    fun addTrack(track: TrackModel)
    fun clearHistory()
    fun saveHistory()
    fun loadHistory()
}