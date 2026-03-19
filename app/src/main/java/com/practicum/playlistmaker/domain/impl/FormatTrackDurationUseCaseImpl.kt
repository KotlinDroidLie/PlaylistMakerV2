package com.practicum.playlistmaker.domain.impl

import java.text.SimpleDateFormat
import java.util.Locale
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackDurationUseCase

class FormatTrackDurationUseCaseImpl : FormatTrackDurationUseCase {
    override fun execute(duration: Int): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration) 
}