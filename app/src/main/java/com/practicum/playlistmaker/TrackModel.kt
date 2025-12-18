package com.practicum.playlistmaker
import android.icu.text.SimpleDateFormat
import com.google.gson.annotations.SerializedName
import java.util.Locale

data class TrackModel(
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackDuration: Int,
    @SerializedName("artworkUrl100") val trackImage: String
){
    fun formatTrackDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDuration)
}
