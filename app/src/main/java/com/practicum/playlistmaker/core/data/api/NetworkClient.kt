package com.practicum.playlistmaker.core.data.api

import com.practicum.playlistmaker.core.data.Response
import com.practicum.playlistmaker.core.data.TrackRequest

interface NetworkClient {
    fun requestTracks(dto: TrackRequest): Response
}