package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.api.IUpdatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult

class UpdatePlaylistUseCase(
    private val repo: IPlaylistRepo,
    private val createPlaylistUseCase: ICreatePlaylistUseCase
) : IUpdatePlaylistUseCase {
    override suspend fun invoke(playlist: PlaylistModel, oldSourceUri: String?): SaveResult {
        oldSourceUri?.let {
            repo.deletePosterImage(it)
        }
        return createPlaylistUseCase(playlist)
    }
}