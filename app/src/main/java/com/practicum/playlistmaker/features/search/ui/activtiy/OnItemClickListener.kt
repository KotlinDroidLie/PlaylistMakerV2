package com.practicum.playlistmaker.features.search.ui.activtiy

import com.practicum.playlistmaker.core.models.TrackModel

interface OnItemClickListener {
    fun addToSearchHistory(track: TrackModel)
    fun openAudioPlayer(track: TrackModel)
}