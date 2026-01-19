package com.practicum.playlistmaker

interface OnItemClickListener {
    fun addToSearchHistory(track: TrackModel)
    fun openAudioPlayer(track: TrackModel)
}