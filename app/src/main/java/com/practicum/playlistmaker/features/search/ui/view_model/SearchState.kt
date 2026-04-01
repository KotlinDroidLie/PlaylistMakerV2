package com.practicum.playlistmaker.features.search.ui.view_model

import androidx.annotation.StringRes
import com.practicum.playlistmaker.features.search.domain.model.TrackModel

sealed interface SearchState {
    object Loading: SearchState
    data class Content(val tracks: List<TrackModel>): SearchState
    data class Error(@field:StringRes val errorMessage: Int): SearchState
    data class Empty(@field:StringRes val emptyMessage: Int): SearchState
    data class History(val history: List<TrackModel>): SearchState
}