package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.data.extensions.toDomainModels
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.api.TrackRepositoryResult
import com.practicum.playlistmaker.domain.api.repo.TrackRepository

class RemoteTrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override fun doRequest(expression: String): TrackRepositoryResult {
        val response = networkClient.requestTracks(TrackRequest(expression))
        return when {
            response.resultCode == 200 -> {
                val tracks = (response as TrackResponse).toDomainModels()
                if (tracks.isEmpty()) TrackRepositoryResult.NotFound
                else TrackRepositoryResult.Success(tracks)
            }
            else -> TrackRepositoryResult.NetworkError
        }
    }
}