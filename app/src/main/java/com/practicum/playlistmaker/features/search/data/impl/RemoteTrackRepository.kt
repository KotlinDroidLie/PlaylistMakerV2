package com.practicum.playlistmaker.features.search.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.search.data.api.NetworkClient
import com.practicum.playlistmaker.features.search.data.dto.ErrorType
import com.practicum.playlistmaker.features.search.data.dto.Resource
import com.practicum.playlistmaker.features.search.data.dto.TrackRequest
import com.practicum.playlistmaker.features.search.data.dto.TrackResponse
import com.practicum.playlistmaker.features.search.data.extensions.toDomain
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteTrackRepository(
    private val networkClient: NetworkClient,
    private val appDataBase: AppDataBase
) : IRemoteTrackRepository {
    override fun doRequest(expression: String): Flow<Resource<List<TrackModel>>> = flow {
        val response = networkClient.requestTracks(TrackRequest(expression))
        when (response.resultCode) {
            200 -> {
                val favouriteTrackIds = appDataBase.trackDao().getFavouriteTracksIds()
                val tracks = (response as TrackResponse).results.map {
                    it.toDomain(favouriteTrackIds)
                }
                emit(Resource.Success(tracks))
            }

            -1 -> emit(Resource.Error(type = ErrorType.NETWORK, message = R.string.placeholder_text_error_network))

            else -> emit(Resource.Error(
                type = ErrorType.GENERIC,
                message = R.string.placeholder_text_error_generic,
                extraMessage = response.resultCode.toString()
            ))
        }
    }
}
