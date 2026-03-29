package com.practicum.playlistmaker.core.data.dto

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("results") val results: List<TrackDto>
): Response()