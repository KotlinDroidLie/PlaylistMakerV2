package com.practicum.playlistmaker.features.search.domain.impl

import com.practicum.playlistmaker.core.models.TrackModel
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IHistoryUseCase

class HistoryUseCase(
    private val historyRepository: ISearchHistoryRepository
): IHistoryUseCase {
    override fun getHistory(): List<TrackModel> {
        return historyRepository.getHistory()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun saveToHistory(track: TrackModel) {
        historyRepository.saveToHistory(track)
    }

}