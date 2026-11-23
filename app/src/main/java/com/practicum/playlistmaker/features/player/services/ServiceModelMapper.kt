package com.practicum.playlistmaker.features.player.services

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

fun TrackModel.toServiceModel(): TrackServiceModel{
    return TrackServiceModel(
        this.trackName,
        this.artistName,
        this.audioPreviewUrl
    )
}