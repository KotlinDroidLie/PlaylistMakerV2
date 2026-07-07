package com.practicum.playlistmaker.features.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val uri: String?,
    val idsTracks: List<Int> = emptyList(),
    val totalTracks: Int = 0
)