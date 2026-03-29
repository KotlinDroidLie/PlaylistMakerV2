package com.practicum.playlistmaker.core.data.extensions

import com.practicum.playlistmaker.core.data.TrackResponse
import com.practicum.playlistmaker.domain.models.TrackModel

fun TrackResponse.toDomainModels(): List<TrackModel>{
    return this.results.map {
        TrackModel(
            trackId = it.trackId,
            trackName = it.trackName,
            artistName = it.artistName,
            albumName = it.albumName,
            releaseDate = it.releaseDate,
            genre = it.genre,
            country = it.country,
            trackDuration = it.trackDuration,
            trackImage = it.trackImage,
            audioPreviewUrl = it.audioPreviewUrl
        )
    }
}

