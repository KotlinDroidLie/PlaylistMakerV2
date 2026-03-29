package com.practicum.playlistmaker.core.data.api

import com.practicum.playlistmaker.core.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") term: String): Call<TrackResponse>
}