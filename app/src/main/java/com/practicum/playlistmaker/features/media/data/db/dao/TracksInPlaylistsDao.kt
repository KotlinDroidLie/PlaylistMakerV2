package com.practicum.playlistmaker.features.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.features.media.data.db.entity.TracksInPlaylistsEntity

@Dao
interface TracksInPlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TracksInPlaylistsEntity)

    @Query("SELECT * FROM tracks_in_playlists WHERE trackId IN (:trackIds)")
    suspend fun getTracksByIds(trackIds: List<Int>): List<TracksInPlaylistsEntity>
}