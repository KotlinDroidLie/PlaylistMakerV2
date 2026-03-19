package com.practicum.playlistmaker.domain.models

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
) : Parcelable {
    fun formatTrackDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDuration)
    fun getCoverArtwork() = trackImage.replaceAfterLast('/',"512x512bb.jpg")
    fun dateFormat(format: String) = SimpleDateFormat(format, Locale.getDefault()).format(releaseDate) ?: ""
}