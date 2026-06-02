package com.practicum.playlistmaker.features.search.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.features.search.data.api.ITunesApi
import com.practicum.playlistmaker.features.search.data.api.NetworkClient
import com.practicum.playlistmaker.features.search.data.dto.Response
import com.practicum.playlistmaker.features.search.data.dto.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val iTunesApiService: ITunesApi
) : NetworkClient {

    override suspend fun requestTracks(dto: TrackRequest): Response {
        if(!isConnected()){
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

    private fun isConnected(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        capabilities?.let {
            when{
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}