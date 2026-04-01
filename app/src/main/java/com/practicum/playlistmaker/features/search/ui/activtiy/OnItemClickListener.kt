package com.practicum.playlistmaker.features.search.ui.activtiy

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

interface OnItemClickListener {
    fun addToSearchHistory(track: TrackModel)
    fun openAudioPlayer(track: TrackModel)
}