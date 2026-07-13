package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

interface IGetPlaylistByIdUseCase {
    suspend operator fun invoke(playlistId: Int): PlaylistModel
}