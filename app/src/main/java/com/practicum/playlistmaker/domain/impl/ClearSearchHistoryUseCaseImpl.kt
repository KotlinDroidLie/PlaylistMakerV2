package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.usecase.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository

class ClearSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : ClearSearchHistoryUseCase {
    override fun execute() {
        historyRepository.clearHistory()
        historyRepository.saveHistory()
    }
}