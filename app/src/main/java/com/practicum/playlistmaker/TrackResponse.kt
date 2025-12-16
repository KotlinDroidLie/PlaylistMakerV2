package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("results") val results: List<TrackModel>
)
