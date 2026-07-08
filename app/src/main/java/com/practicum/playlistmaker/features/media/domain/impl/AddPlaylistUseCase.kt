package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IAddPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

class AddPlaylistUseCase(
    private val repo: IPlaylistRepo
) : IAddPlaylistUseCase {
    override suspend fun insertPlaylist(playlist: PlaylistModel) {
        repo.insertPlaylist(playlist)
    }
}