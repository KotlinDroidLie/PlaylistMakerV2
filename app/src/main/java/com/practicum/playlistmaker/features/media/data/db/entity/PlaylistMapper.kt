package com.practicum.playlistmaker.features.media.data.db.entity

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

fun PlaylistEntity.toModel(): PlaylistModel{
    return PlaylistModel(
        id = id,
        title = title,
        description = description,
        uri = uri,
        idsTracks = idsTracks,
        totalTracks = totalTracks
    )
}

fun PlaylistModel.toEntity(): PlaylistEntity{
    return PlaylistEntity(
        title = title,
        description = description,
        uri = uri,
        idsTracks = idsTracks,
        totalTracks = totalTracks
    )
}