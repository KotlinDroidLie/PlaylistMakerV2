package com.practicum.playlistmaker.features.media.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.features.media.data.converters.Converters


@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}