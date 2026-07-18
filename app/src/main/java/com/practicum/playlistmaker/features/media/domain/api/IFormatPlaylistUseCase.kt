package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

interface IFormatPlaylistUseCase {
    fun getTracksDuration(tracks: List<TrackModel>): Int
}