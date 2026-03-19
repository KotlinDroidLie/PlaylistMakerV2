package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.api.usecase.SaveSearchHistoryUseCase

class SaveSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : SaveSearchHistoryUseCase {
    override fun execute() {
        historyRepository.saveHistory()
    }
}