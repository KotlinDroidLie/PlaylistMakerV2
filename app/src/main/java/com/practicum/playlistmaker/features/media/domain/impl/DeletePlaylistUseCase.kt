package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IDeletePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.DeleteResult

class DeletePlaylistUseCase(
    private val repo: IPlaylistRepo
) : IDeletePlaylistUseCase {
    override suspend fun invoke(playlistId: Int): DeleteResult {
        return repo.deletePlaylist(playlistId)
    }
}