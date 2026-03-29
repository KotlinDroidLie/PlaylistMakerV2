package com.practicum.playlistmaker.features.search.ui

import com.practicum.playlistmaker.core.TrackModel

interface OnItemClickListener {
    fun addToSearchHistory(track: TrackModel)
    fun openAudioPlayer(track: TrackModel)
}