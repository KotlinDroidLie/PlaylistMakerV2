package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.api.IRemoveTrackUseCase

class RemoveTrackUseCase(
    private val repo: IPlaylistRepo
) : IRemoveTrackUseCase {
    override suspend fun invoke(trackId: Int, playlistId: Int) {
        repo.removeTrackFromPlaylist(trackId, playlistId)
    }
}