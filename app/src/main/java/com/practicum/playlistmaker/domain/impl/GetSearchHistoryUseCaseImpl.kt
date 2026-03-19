package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.usecase.GetSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.models.TrackModel

class GetSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : GetSearchHistoryUseCase {
    override fun execute() = historyRepository.getHistory()
}