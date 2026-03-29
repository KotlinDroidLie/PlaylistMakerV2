package com.practicum.playlistmaker.features.player.domain.impl

import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackYearUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatTrackYearUseCase : IFormatTrackYearUseCase {
    private val pattern = "yyyy"
    override fun execute(date: Date?): String {
        return if (date != null) {
            SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        } else {
            ""
        }
    }
}