package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface IPlaylistInteractor {
    suspend fun insertPlaylist(playlist: PlaylistModel)
    fun getPlaylists(): Flow<List<PlaylistModel>>
}