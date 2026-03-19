package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.usecase.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.api.repo.HistoryTracksRepository
import com.practicum.playlistmaker.domain.models.TrackModel

class AddTrackToHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : AddTrackToHistoryUseCase {
    override fun execute(track: TrackModel) {
        historyRepository.addTrack(track)
        historyRepository.saveHistory()
    }
}