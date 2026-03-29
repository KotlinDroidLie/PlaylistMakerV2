package com.practicum.playlistmaker.core.data

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.core.data.dto.TrackDto

data class TrackResponse(
    @SerializedName("results") val results: List<TrackDto>
): Response()