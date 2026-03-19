package com.practicum.playlistmaker.data.repository

import android.content.Context
import androidx.core.content.edit
import com.practicum.playlistmaker.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.data.extensions.toDomain
import com.practicum.playlistmaker.data.extensions.toDto
import com.practicum.playlistmaker.di.GsonSingleton.gson
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.models.TrackModel
import kotlin.collections.addAll

class  LocalHistoryTracksRepository(context: Context): HistoryTracksRepository {
    private val historySearchSharedPreferences = context.getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    private val trackHistoryList = mutableListOf<TrackHistoryDto>()

    override fun getHistory(): List<TrackModel> {
        return trackHistoryList.map { it.toDomain() }.toMutableList()
    }

    override fun addTrack(track: TrackModel) {
        val dto = track.toDto()
        val removeTrackId = trackHistoryList.indexOfFirst { it.trackId == dto.trackId }
        if (removeTrackId != -1) {
            trackHistoryList.removeAt(removeTrackId)
        } else if (trackHistoryList.size == MAX_SIZE) {
            trackHistoryList.removeAt(MAX_SIZE - 1)
        }
        trackHistoryList.add(0, dto)
    }

    override fun clearHistory() {
        trackHistoryList.clear()
    }

    override fun saveHistory() {
        val json = gson.toJson(trackHistoryList)
        historySearchSharedPreferences.edit{
            putString(KEY_HISTORY_TRACK,json)
        }
    }

    override fun loadHistory() {
        val json = historySearchSharedPreferences.getString(KEY_HISTORY_TRACK, null)
        if (json != null){
            trackHistoryList.clear()
            trackHistoryList.addAll(gson.fromJson(json, Array<TrackHistoryDto>::class.java))
        } else {
            trackHistoryList.clear()
        }
    }

    private companion object{
        private const val MAX_SIZE = 10
        const val KEY_HISTORY_TRACK = "key_history_track"
        const val HISTORY_PREFERENCES = "history_preferences"
    }
}