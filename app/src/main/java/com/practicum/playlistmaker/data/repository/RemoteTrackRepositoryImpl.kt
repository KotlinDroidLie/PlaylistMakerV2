package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.data.extensions.toDomainModels
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.api.repo.TrackRepository
import com.practicum.playlistmaker.domain.models.TrackModel

class RemoteTrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override fun doRequest(expression: String): List<TrackModel> {
        val response = networkClient.requestTracks(TrackRequest(expression))
        if(response.resultCode == 200){
            return (response as TrackResponse).toDomainModels()
        } else {
            return emptyList()
        }
    }
}