package com.practicum.playlistmaker.features.player.domain.api

import java.util.Date

interface IFormatTrackUseCase {
    fun getTrackDuration(duration: Int): String
    fun getTrackYear(date: Date?): String
    fun getCoverArtwork(trackImage: String): String
}