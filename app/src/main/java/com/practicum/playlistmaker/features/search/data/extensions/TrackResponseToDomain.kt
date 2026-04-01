package com.practicum.playlistmaker.features.search.data.extensions

import com.practicum.playlistmaker.features.search.data.dto.TrackResponse
import com.practicum.playlistmaker.features.search.domain.model.TrackModel

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

