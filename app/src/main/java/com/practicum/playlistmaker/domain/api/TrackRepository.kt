package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.TrackModel

interface TrackRepository {
    fun doRequest(expression: String): List<TrackModel>
}