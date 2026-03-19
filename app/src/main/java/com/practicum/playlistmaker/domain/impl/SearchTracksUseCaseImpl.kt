package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.usecase.SearchTracksUseCase
import com.practicum.playlistmaker.domain.api.repo.TrackRepository
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.domain.api.TrackRepositoryResult

class SearchTracksUseCaseImpl(
    private val repository: TrackRepository,
    private val mainHandler: Handler = Handler(Looper.getMainLooper())
):  SearchTracksUseCase {

    override fun searchTracks(expression: String, consumer: SearchTracksUseCase.TracksConsumer) {
        Thread {
            val result = try {
                repository.doRequest(expression)
            } catch (e: Exception) {
                TrackRepositoryResult.NetworkError
            }

            mainHandler.post {
                consumer.consume(result)
            }
        }.start()
    }

}