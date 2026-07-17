package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.DeleteResult

interface IDeletePlaylistUseCase {
    suspend operator fun invoke(playlistId: Int): DeleteResult
}