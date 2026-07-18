package com.practicum.playlistmaker.features.sharing.domain.model

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

data class TrackShareModel(
    val trackName: String,
    val artistName: String,
    val trackDuration: Int,
)


fun TrackModel.toShareModel(): TrackShareModel{
    return TrackShareModel(
        trackName = this.trackName,
        artistName = this.artistName,
        trackDuration = this.trackDuration
    )
}