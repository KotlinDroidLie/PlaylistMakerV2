package com.practicum.playlistmaker.features.player.domain.api

interface IFormatTrackDurationUseCase {
    fun execute(duration: Int): String
}