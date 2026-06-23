package com.practicum.playlistmaker.features.media.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun removeTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getFavouriteTracks() : List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getFavouriteTracksId(): List<Int>
}