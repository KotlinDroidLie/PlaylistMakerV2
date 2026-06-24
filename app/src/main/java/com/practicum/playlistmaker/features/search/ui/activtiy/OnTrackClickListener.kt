package com.practicum.playlistmaker.features.search.ui.activtiy

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

interface OnTrackClickListener {
    fun openAudioPlayer(track: TrackModel)
}