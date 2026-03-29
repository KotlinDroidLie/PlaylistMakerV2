package com.practicum.playlistmaker.domainTerminated.impl

import com.practicum.playlistmaker.domainTerminated.api.repoTerminated.HistoryTracksRepository
import com.practicum.playlistmaker.domainTerminated.api.usecase.LoadSearchHistoryUseCase

class LoadSearchHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : LoadSearchHistoryUseCase {
    override fun execute(){
        historyRepository.loadHistory()
    }
}