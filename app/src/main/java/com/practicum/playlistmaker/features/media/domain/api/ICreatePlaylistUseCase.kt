package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult

interface ICreatePlaylistUseCase {
    suspend operator fun invoke(playlist: PlaylistModel) : SaveResult
}

