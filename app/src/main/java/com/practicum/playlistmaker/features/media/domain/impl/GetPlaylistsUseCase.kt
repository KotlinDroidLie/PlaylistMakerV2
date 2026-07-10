package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetPlaylistsUseCase(
    private val repo: IPlaylistRepo
) : IGetPlaylistsUseCase {
    override fun invoke(): Flow<List<PlaylistModel>> {
        return repo.getPlaylists()
    }
}