package com.practicum.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
class SearchHistory(private val historySearchSharedPreferences: SharedPreferences) {
    private val trackHistoryList = mutableListOf<TrackModel>()

    fun write(track: TrackModel) {
        val removeTrackId = trackHistoryList.indexOfFirst { it.trackId == track.trackId }
        if (removeTrackId != -1) {
            trackHistoryList.removeAt(removeTrackId)
        } else if (trackHistoryList.size == MAX_SIZE) {
            trackHistoryList.removeAt(MAX_SIZE - 1)
        }
        trackHistoryList.add(0, track)
    }
    fun clear(){
        trackHistoryList.clear()
    }
     fun saveToPreference(){
        val json = gson.toJson(trackHistoryList)
        historySearchSharedPreferences.edit{
            putString(KEY_HISTORY_TRACK,json)
        }
    }
    fun read() = trackHistoryList
     fun loadFromPreference(){
        val json = historySearchSharedPreferences.getString(KEY_HISTORY_TRACK, null)
        if (json != null){
            trackHistoryList.clear()
            trackHistoryList.addAll(gson.fromJson(json, Array<TrackModel>::class.java))
        } else {
            trackHistoryList.clear()
        }
    }
    companion object{
        private const val MAX_SIZE = 10
        const val KEY_HISTORY_TRACK = "key_history_track"
        private val gson = Gson()
    }
}