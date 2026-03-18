package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.AddTrackToHistoryUseCase
import com.practicum.playlistmaker.domain.api.HistoryTracksRepository
import com.practicum.playlistmaker.domain.models.TrackModel

class AddTrackToHistoryUseCaseImpl(
    private val historyRepository: HistoryTracksRepository
) : AddTrackToHistoryUseCase {
    override fun invoke(track: TrackModel) {
        historyRepository.addTrack(track)
        historyRepository.saveHistory()
    }
}