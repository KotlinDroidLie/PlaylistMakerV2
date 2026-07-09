package com.practicum.playlistmaker.features.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "tracks_in_playlists")
data class TracksInPlaylistsEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val albumName: String?,
    val releaseDate: Date?,
    val genre: String,
    val country: String,
    val trackDuration: Int,
    val trackImage: String,
    val audioPreviewUrl: String
)
