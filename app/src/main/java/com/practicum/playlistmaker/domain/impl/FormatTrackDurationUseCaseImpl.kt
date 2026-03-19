package com.practicum.playlistmaker.domain.impl

import java.text.SimpleDateFormat
import java.util.Locale
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackDurationUseCase

class FormatTrackDurationUseCaseImpl : FormatTrackDurationUseCase {
    private val pattern = "mm:ss"
    override fun execute(duration: Int): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(duration)
    }
}