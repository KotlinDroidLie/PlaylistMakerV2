package com.practicum.playlistmaker.features.search.ui.activtiy

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

interface OnTrackHistoryClickListener {
    fun addToSearchHistory(track: TrackModel)
}