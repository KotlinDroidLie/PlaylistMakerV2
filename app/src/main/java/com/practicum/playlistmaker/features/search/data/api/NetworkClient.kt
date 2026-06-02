package com.practicum.playlistmaker.features.search.data.api

import com.practicum.playlistmaker.features.search.data.dto.Response
import com.practicum.playlistmaker.features.search.data.dto.TrackRequest

interface NetworkClient {
    suspend fun requestTracks(dto: TrackRequest): Response
}