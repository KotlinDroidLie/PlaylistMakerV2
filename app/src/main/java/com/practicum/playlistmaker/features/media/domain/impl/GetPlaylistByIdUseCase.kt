package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

class GetPlaylistByIdUseCase(
    private val repo: IPlaylistRepo
) : IGetPlaylistByIdUseCase {
    override suspend fun invoke(playlistId: Int): PlaylistModel {
        return repo.getPlaylistById(playlistId)
    }
}