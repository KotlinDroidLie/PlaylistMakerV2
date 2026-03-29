package com.practicum.playlistmaker.features.search.data

import com.practicum.playlistmaker.core.data.dto.ErrorType
import com.practicum.playlistmaker.core.data.dto.Resource
import com.practicum.playlistmaker.core.data.dto.TrackRequest
import com.practicum.playlistmaker.core.data.dto.TrackResponse
import com.practicum.playlistmaker.core.data.api.NetworkClient
import com.practicum.playlistmaker.core.data.extensions.toDomainModels
import com.practicum.playlistmaker.domain.models.TrackModel
import com.practicum.playlistmaker.features.search.domain.api.ITrackRepository

class RemoteTrackRepository(private val networkClient: NetworkClient): ITrackRepository {
    override fun doRequest(expression: String): Resource<List<TrackModel>> {
        return try{
            val response = networkClient.requestTracks(TrackRequest(expression))
            when(response.resultCode){
                200 ->{
                    val tracks = (response as TrackResponse).toDomainModels()
                    Resource.Success(tracks)
                }
                -1 -> Resource.Error(type = ErrorType.NETWORK, message = "Отсутствует подключение к интернету!")
                else -> Resource.Error(type = ErrorType.GENERIC, message = "Ошибка сервера ${response.resultCode}")
            }
        } catch (e: Exception){
            return Resource.Error(type = ErrorType.EXCEPTION, message = e.message)
        }
    }
}