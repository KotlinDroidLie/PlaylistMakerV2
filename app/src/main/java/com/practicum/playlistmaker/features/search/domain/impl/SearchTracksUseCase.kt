package com.practicum.playlistmaker.features.search.domain.impl

import com.practicum.playlistmaker.features.search.data.dto.Resource
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase

class SearchTracksUseCase(
    private val repository: IRemoteTrackRepository,
): ISearchTracksUseCase {

    override fun searchTracks(expression: String, consumer: ISearchTracksUseCase.TracksConsumer) {
        Thread{
            when(val result = repository.doRequest(expression)){
                is Resource.Error -> { consumer.consume(result.data, result.message, result.type) }
                is Resource.Success -> { consumer.consume(result.data, null, null) }
            }
        }.start()
    }

}