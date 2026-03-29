package com.practicum.playlistmaker.features.search.domain.impl

import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IClearSearchHistoryUseCase

class ClearSearchHistoryUseCase(
    private val historyRepository: ISearchHistoryRepository
) : IClearSearchHistoryUseCase {
    override fun execute() {
        historyRepository.clearHistory()
    }
}