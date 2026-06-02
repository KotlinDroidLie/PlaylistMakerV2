package com.practicum.playlistmaker.features.search.data.api

import com.practicum.playlistmaker.features.search.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") term: String): TrackResponse
}