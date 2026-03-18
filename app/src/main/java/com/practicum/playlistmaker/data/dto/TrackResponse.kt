package com.practicum.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.domain.models.TrackModel

data class TrackResponse(
    @SerializedName("results") val results: List<TrackDto>
): Response()