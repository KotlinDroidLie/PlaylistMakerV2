package com.practicum.playlistmaker.features.search.ui.view_model

import com.practicum.playlistmaker.features.search.domain.model.TrackModel

sealed interface SearchState {
    object Loading: SearchState
    data class Content(val tracks: List<TrackModel>): SearchState
    data class Error(val errorMessage: String): SearchState
    data class Empty(val emptyMessage:String): SearchState
}