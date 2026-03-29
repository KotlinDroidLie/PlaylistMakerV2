package com.practicum.playlistmaker.features.player.domain.impl

import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatTrackUseCase: IFormatTrackUseCase {
    private val yearPattern = "yyyy"
    private val durationPattern = "mm:ss"
    override fun getTrackDuration(duration: Int): String {
        return SimpleDateFormat(durationPattern, Locale.getDefault()).format(duration)
    }

    override fun getTrackYear(date: Date?): String {
        return if (date != null) {
            SimpleDateFormat(yearPattern, Locale.getDefault()).format(date)
        } else {
            ""
        }
    }
}