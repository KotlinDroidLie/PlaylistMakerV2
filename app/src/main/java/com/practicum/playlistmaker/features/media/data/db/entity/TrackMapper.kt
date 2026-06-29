package com.practicum.playlistmaker.features.media.data.db.entity

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

fun TrackEntity.toModel(): TrackModel{
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
        isFavourite = true
    )
}


fun TrackModel.toEntity(): TrackEntity{
    return TrackEntity(
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