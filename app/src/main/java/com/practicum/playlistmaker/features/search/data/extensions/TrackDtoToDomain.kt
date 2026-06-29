package com.practicum.playlistmaker.features.search.data.extensions

import com.practicum.playlistmaker.features.search.data.dto.TrackDto
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
fun TrackDto.toDomain(favouriteIds: List<Int>): TrackModel{
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
        audioPreviewUrl = audioPreviewUrl,
        isFavourite = trackId in favouriteIds
    )
}

