package com.practicum.playlistmaker.features.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "track_table")
data class TrackEntity(
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