package com.practicum.playlistmaker.features.media.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel(): ViewModel() {
    private val _state = MutableLiveData<PlaylistState>(PlaylistState.Empty)
    val state: LiveData<PlaylistState> = _state
}