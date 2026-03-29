package com.practicum.playlistmaker.features.player.domain.impl

import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackDurationUseCase
import java.text.SimpleDateFormat
import java.util.Locale

class FormatTrackDurationUseCase : IFormatTrackDurationUseCase {
    private val pattern = "mm:ss"
    override fun execute(duration: Int): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(duration)
    }
}