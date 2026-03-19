package com.practicum.playlistmaker.data.extensions

import com.practicum.playlistmaker.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.domain.models.TrackModel

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
