package com.practicum.playlistmaker.features.search.domain.impl

import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IAddTrackToHistoryUseCase

class AddTrackToHistoryUseCase(
    private val historyRepository: ISearchHistoryRepository
) : IAddTrackToHistoryUseCase {
    override fun execute(track: TrackModel) {
        historyRepository.saveToHistory(track)
    }
}