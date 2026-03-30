package com.practicum.playlistmaker.core

import android.icu.text.SimpleDateFormat
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.Locale

@Parcelize
data class TrackModel(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val albumName: String?,
    val releaseDate: Date?,
    val genre: String,
    val country: String,
    val trackDuration: Int,
    val trackImage: String,
    val audioPreviewUrl: String
) : Parcelable