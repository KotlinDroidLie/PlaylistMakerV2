package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.ClearSearchHistoryUseCase
import com.practicum.playlistmaker.domain.api.HistoryTracksRepository

class ClearSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : ClearSearchHistoryUseCase {
    override fun invoke() {
        historyRepository.clearHistory()
        historyRepository.saveHistory()
    }
}