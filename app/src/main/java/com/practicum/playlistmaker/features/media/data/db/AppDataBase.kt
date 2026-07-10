package com.practicum.playlistmaker.features.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.features.media.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.features.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.features.media.data.db.converters.Converters
import com.practicum.playlistmaker.features.media.data.db.dao.TrackDao
import com.practicum.playlistmaker.features.media.data.db.dao.TracksInPlaylistsDao
import com.practicum.playlistmaker.features.media.data.db.entity.TrackEntity
import com.practicum.playlistmaker.features.media.data.db.entity.TracksInPlaylistsEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TracksInPlaylistsEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracksInPlaylistsDao(): TracksInPlaylistsDao
}