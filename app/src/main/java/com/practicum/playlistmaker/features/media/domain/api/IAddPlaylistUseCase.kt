package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

interface IAddPlaylistUseCase {
    suspend fun insertPlaylist(playlist: PlaylistModel)
}