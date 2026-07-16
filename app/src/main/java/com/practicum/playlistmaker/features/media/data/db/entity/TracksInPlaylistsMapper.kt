package com.practicum.playlistmaker.features.media.data.db.entity

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

fun TrackModel.toTracksInPlaylistsEntity():TracksInPlaylistsEntity{
    return TracksInPlaylistsEntity(
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
        isFavourite = isFavourite,
    )
}

fun TracksInPlaylistsEntity.toModel(): TrackModel{
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
        isFavourite = isFavourite,
        )
}
