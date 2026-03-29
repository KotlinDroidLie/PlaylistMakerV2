package com.practicum.playlistmaker.core.data.api

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackRequest

interface NetworkClient {
    fun requestTracks(dto: TrackRequest): Response
}