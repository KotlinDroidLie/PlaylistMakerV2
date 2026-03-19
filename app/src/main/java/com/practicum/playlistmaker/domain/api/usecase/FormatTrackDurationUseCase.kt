package com.practicum.playlistmaker.domain.api.usecase

interface FormatTrackDurationUseCase {
    fun execute(duration: Int): String
}