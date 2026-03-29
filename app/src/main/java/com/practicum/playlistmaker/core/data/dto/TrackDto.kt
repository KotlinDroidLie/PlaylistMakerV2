package com.practicum.playlistmaker.core.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TrackDto(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("collectionName") val albumName: String?,
    @SerializedName("releaseDate") val releaseDate: Date?,
    @SerializedName("primaryGenreName") val genre: String,
    @SerializedName("country") val country: String,
    @SerializedName("trackTimeMillis") val trackDuration: Int,
    @SerializedName("artworkUrl100") val trackImage: String,
    @SerializedName("previewUrl") val audioPreviewUrl: String
)