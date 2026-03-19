package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.api.usecase.LoadSearchHistoryUseCase

class LoadSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : LoadSearchHistoryUseCase {
    override fun execute(){
        historyRepository.loadHistory()
    }
}