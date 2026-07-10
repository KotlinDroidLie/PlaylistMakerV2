package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface IGetPlaylistsUseCase {
    operator fun invoke(): Flow<List<PlaylistModel>>
}