package com.practicum.playlistmaker.features.media.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavouriteTracksViewModel(): ViewModel() {
    private val _state = MutableLiveData<FavouriteTracksState>(FavouriteTracksState.Empty)
    val state: LiveData<FavouriteTracksState> = _state
}