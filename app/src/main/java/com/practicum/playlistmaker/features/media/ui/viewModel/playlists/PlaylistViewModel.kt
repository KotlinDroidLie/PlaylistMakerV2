package com.practicum.playlistmaker.features.media.ui.viewModel.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistsUseCase: IGetPlaylistsUseCase
): ViewModel() {
    private val _state = MutableLiveData<PlaylistsState>(PlaylistsState.Empty)
    val state: LiveData<PlaylistsState> = _state
    init {
        viewModelScope.launch {
            getPlaylistsUseCase().collect { playlistModels ->
                processResult(playlistModels)
            }
        }
    }
    private fun processResult(playlistModels: List<PlaylistModel>){
        if(playlistModels.isEmpty()){
            renderState(PlaylistsState.Empty)
        } else {
            renderState(PlaylistsState.Content(playlistModels))
        }
    }
    private fun renderState(state: PlaylistsState){
        _state.postValue(state)
    }
}