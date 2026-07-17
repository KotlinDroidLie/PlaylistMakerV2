package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.DeleteResult
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface IPlaylistRepo {
    suspend fun savePosterImage(sourceUri: String) : SaveResult
    suspend fun insertPlaylist(playlist: PlaylistModel): SaveResult
    fun getPlaylists(): Flow<List<PlaylistModel>>
    suspend fun insertTrackInPlaylist(track: TrackModel, playlist: PlaylistModel): SaveResult
    suspend fun getPlaylistById(playlistId: Int): PlaylistModel
    fun getTracksByIds(tracksIds: List<Int>): Flow<List<TrackModel>>
    suspend fun removeTrackFromPlaylist(trackId: Int, playlistId: Int)
    suspend fun deletePlaylist(playlistId: Int): DeleteResult
}