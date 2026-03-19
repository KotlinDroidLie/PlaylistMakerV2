package com.practicum.playlistmaker.domain.impl

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackYearUseCase

class FormatTrackYearUseCaseImpl() : FormatTrackYearUseCase {
    private val pattern = "yyyy"
    override fun execute(date: Date?): String {
        return if (date != null) {
            SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        } else {
            ""
        }
    }
}