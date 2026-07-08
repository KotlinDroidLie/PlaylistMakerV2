package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import kotlinx.coroutines.flow.Flow

interface IPlaylistInteractor {
    suspend fun createPlaylist(playlist: PlaylistModel) : SaveResult
    fun getPlaylists(): Flow<List<PlaylistModel>>
}

