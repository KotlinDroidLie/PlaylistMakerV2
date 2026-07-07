package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow

class PlaylistInteractor(
    private val repo: IPlaylistRepo
) : IPlaylistInteractor {
    override suspend fun insertPlaylist(playlist: PlaylistModel) {
        repo.insertPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return repo.getPlaylists()
    }
}