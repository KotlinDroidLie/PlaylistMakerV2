package com.practicum.playlistmaker.features.media.domain.api

interface IRemoveTrackUseCase {
    suspend operator fun invoke(trackId: Int, playlistId: Int)
}