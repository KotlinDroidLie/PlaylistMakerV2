package com.practicum.playlistmaker
import android.icu.text.SimpleDateFormat
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.Locale

@Parcelize
data class TrackModel(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("collectionName") val albumName: String?,
    @SerializedName("releaseDate") val releaseDate: Date?,
    @SerializedName("primaryGenreName") val genre: String,
    @SerializedName("country") val country: String,
    @SerializedName("trackTimeMillis") val trackDuration: Int,
    @SerializedName("artworkUrl100") val trackImage: String
) : Parcelable {
    fun formatTrackDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDuration)
    fun getCoverArtwork() = trackImage.replaceAfterLast('/',"512x512bb.jpg")
    fun dateFormat(format: String) = SimpleDateFormat(format, Locale.getDefault()).format(releaseDate) ?: ""
}
