package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult

interface IUpdatePlaylistUseCase {
    suspend operator fun invoke(playlist: PlaylistModel, oldSourceUri: String?): SaveResult
}