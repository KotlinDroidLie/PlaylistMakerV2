package com.practicum.playlistmaker.features.search.data.api

import com.practicum.playlistmaker.features.search.data.dto.Response
import com.practicum.playlistmaker.features.search.data.dto.TrackRequest

interface NetworkClient {
    fun requestTracks(dto: TrackRequest): Response
}