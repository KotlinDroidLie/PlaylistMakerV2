package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.TrackModel

interface GetSearchHistoryUseCase {
    operator fun invoke(): List<TrackModel>
}