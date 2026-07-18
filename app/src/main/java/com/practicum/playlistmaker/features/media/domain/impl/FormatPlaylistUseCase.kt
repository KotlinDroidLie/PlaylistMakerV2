package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IFormatPlaylistUseCase
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import java.text.SimpleDateFormat
import java.util.Locale

class FormatPlaylistUseCase : IFormatPlaylistUseCase {
    private val durationPattern = "mm"
    override fun getTracksDuration(tracks: List<TrackModel>): Int {
        val totalDuration = tracks.sumOf { it.trackDuration }
        return SimpleDateFormat(durationPattern, Locale.getDefault()).format(totalDuration).toInt()
    }
}
