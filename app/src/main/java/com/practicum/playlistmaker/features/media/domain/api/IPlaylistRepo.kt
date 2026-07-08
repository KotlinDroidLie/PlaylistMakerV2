package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import kotlinx.coroutines.flow.Flow

interface IPlaylistRepo {
    suspend fun savePosterImage(sourceUri: String) : SaveResult
    suspend fun insertPlaylist(playlist: PlaylistModel): SaveResult
    fun getPlaylists(): Flow<List<PlaylistModel>>
}