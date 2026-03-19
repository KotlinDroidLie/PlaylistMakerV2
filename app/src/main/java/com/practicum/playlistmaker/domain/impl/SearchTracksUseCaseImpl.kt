package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.usecase.SearchTracksUseCase
import java.util.concurrent.Executors
import com.practicum.playlistmaker.domain.api.repo.TrackRepository

class SearchTracksUseCaseImpl(private val repository: TrackRepository):  SearchTracksUseCase {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchTracksUseCase.TracksConsumer) {
        executor.execute{}
        consumer.consume(repository.doRequest(expression))
    }

}