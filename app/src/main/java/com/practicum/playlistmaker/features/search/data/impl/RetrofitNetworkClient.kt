package com.practicum.playlistmaker.features.search.data.impl

import android.content.Context
import com.practicum.playlistmaker.features.search.data.api.ITunesApi
import com.practicum.playlistmaker.features.search.data.api.NetworkClient
import com.practicum.playlistmaker.features.search.data.dto.Response
import com.practicum.playlistmaker.features.search.data.dto.TrackRequest
import com.practicum.playlistmaker.utils.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val iTunesApiService: ITunesApi
) : NetworkClient {

    override suspend fun requestTracks(dto: TrackRequest): Response {
        if(!context.isConnected()){
            return Response().apply { resultCode = -1 }
        }
        return withContext(Dispatchers.IO){
            try {
                val response = iTunesApiService.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Exception){
                Response().apply { resultCode = 500 }
            }
        }
    }
}