package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow

interface ISearchTracksUseCase {
    fun searchTracks(expression: String) : Flow<SearchResult>
}


sealed class SearchResult{
    data class Success(val tracks: List<TrackModel>?) : SearchResult()
    data class Error(val errorMessage: Int?, val extraMessage: String?) : SearchResult()
}