package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackRequest

interface NetworkClient {
    fun requestTracks(dto: TrackRequest): Response
}