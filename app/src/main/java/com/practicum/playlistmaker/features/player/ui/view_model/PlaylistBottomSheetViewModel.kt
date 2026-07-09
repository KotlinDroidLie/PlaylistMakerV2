package com.practicum.playlistmaker.features.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.launch

class PlaylistBottomSheetViewModel(
    private val getPlaylistsUseCase: IGetPlaylistsUseCase
): ViewModel() {
    private var _state = MutableLiveData<PlaylistBottomSheetState>(PlaylistBottomSheetState.Hide)
    val state: LiveData<PlaylistBottomSheetState> = _state

    fun openBottomSheet(){
        viewModelScope.launch {
            getPlaylistsUseCase().collect { playlistModels ->
                processResult(playlistModels)
            }
        }
    }

    private fun processResult(playlistModels: List<PlaylistModel>){
        if(playlistModels.isEmpty()){
            renderState(PlaylistBottomSheetState.Empty)
        } else {
            renderState(PlaylistBottomSheetState.Content(playlistModels))
        }
    }

    private fun renderState(state: PlaylistBottomSheetState){
        _state.postValue(state)
    }
}