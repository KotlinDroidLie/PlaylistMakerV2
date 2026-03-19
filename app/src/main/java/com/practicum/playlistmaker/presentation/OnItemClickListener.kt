package com.practicum.playlistmaker.presentation

import com.practicum.playlistmaker.domain.models.TrackModel

interface OnItemClickListener {
    fun addToSearchHistory(track: TrackModel)
    fun openAudioPlayer(track: TrackModel)
}