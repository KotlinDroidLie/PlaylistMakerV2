package com.practicum.playlistmaker.features.search.data.extensions

import com.practicum.playlistmaker.features.search.data.dto.TrackHistoryDto
import com.practicum.playlistmaker.core.models.TrackModel

fun TrackModel.toDto(): TrackHistoryDto = TrackHistoryDto(
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