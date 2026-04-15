package com.practicum.playlistmaker.features.media.ui.viewModel

import androidx.annotation.StringRes
import com.practicum.playlistmaker.features.search.domain.model.TrackModel

sealed interface FavouriteTracksState {
    data class Content(val favouriteTracks: List<TrackModel>): FavouriteTracksState
    object Empty: FavouriteTracksState
}