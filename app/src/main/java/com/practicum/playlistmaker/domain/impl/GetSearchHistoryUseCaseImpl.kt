package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.HistoryTracksRepository
import com.practicum.playlistmaker.domain.models.TrackModel

class GetSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : GetSearchHistoryUseCase {
    override fun invoke() = historyRepository.getHistory()
}