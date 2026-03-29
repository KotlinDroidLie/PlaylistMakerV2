package com.practicum.playlistmaker.features.search.domain.impl

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.core.data.dto.Resource
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase

class SearchTracksUseCase(
    private val repository: IRemoteTrackRepository,
    private val handler: Handler = Handler(Looper.getMainLooper())
): ISearchTracksUseCase {

    override fun searchTracks(expression: String, consumer: ISearchTracksUseCase.TracksConsumer) {
        Thread{
            when(val result = repository.doRequest(expression)){
                is Resource.Error -> {
                    handler.post {
                        consumer.consume(result.data, result.message, result.type)
                    }
                }
                is Resource.Success -> {
                    handler.post {
                        consumer.consume(result.data, null, null)
                    }
                }
            }
        }.start()
    }

}