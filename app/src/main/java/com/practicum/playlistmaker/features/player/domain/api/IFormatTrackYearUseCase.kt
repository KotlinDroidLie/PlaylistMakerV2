package com.practicum.playlistmaker.features.player.domain.api

import java.util.Date

interface IFormatTrackYearUseCase {
    fun execute(date: Date?): String
}