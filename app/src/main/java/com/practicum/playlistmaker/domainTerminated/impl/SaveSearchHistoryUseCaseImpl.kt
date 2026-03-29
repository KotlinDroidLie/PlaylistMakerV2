package com.practicum.playlistmaker.domainTerminated.impl

import com.practicum.playlistmaker.domainTerminated.api.repoTerminated.HistoryTracksRepository
import com.practicum.playlistmaker.domainTerminated.api.usecase.SaveSearchHistoryUseCase

class SaveSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : SaveSearchHistoryUseCase {
    override fun execute() {
        historyRepository.saveHistory()
    }
}