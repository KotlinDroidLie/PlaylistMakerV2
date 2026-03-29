package com.practicum.playlistmaker.core.data.impl

import com.practicum.playlistmaker.core.data.api.ITunesApi
import com.practicum.playlistmaker.core.data.api.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(I_TUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApi = retrofit.create(ITunesApi::class.java)

    override fun requestTracks(dto: TrackRequest): Response {
        val response = iTunesApi.search(dto.expression).execute()
        val body = response.body() ?: Response()
        return body.apply { resultCode = response.code() }
    }
    companion object{
        private const val I_TUNES_BASE_URL = "https://itunes.apple.com"
    }

}