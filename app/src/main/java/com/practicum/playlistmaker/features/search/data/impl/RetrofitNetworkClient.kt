package com.practicum.playlistmaker.features.search.data.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.features.search.data.api.ITunesApi
import com.practicum.playlistmaker.features.search.data.api.NetworkClient
import com.practicum.playlistmaker.features.search.data.dto.Response
import com.practicum.playlistmaker.features.search.data.dto.TrackRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val iTunesApiService: ITunesApi
) : NetworkClient {

    override fun requestTracks(dto: TrackRequest): Response {
        if(!isConnected()){
            return Response().apply { resultCode = -1 }
        }
        val response = iTunesApiService.search(dto.expression).execute()
        val body = response.body() ?: Response()
        return body.apply { resultCode = response.code() }
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