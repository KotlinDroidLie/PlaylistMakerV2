package com.practicum.playlistmaker.features.player.services

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackServiceModel(
    val trackName: String,
    val artistName: String,
    val audioPreviewUrl: String,
): Parcelable

