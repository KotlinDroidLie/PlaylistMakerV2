package com.practicum.playlistmaker.core.data.extensions

import com.practicum.playlistmaker.core.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.core.TrackModel

fun TrackHistoryDto.toDomain(): TrackModel{
    return TrackModel(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        albumName = albumName,
        releaseDate = releaseDate,
        genre = genre,
        country = country,
        trackDuration = trackDuration,
        trackImage = trackImage,
        audioPreviewUrl = audioPreviewUrl
    )
}
