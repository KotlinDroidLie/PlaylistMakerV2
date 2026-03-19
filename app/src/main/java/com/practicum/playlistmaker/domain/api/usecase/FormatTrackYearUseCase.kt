package com.practicum.playlistmaker.domain.api.usecase

import java.util.Date

interface FormatTrackYearUseCase {
    fun execute(date: Date?): String
}