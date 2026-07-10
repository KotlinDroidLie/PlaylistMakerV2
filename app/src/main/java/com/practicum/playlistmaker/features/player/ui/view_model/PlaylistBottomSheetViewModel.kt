package com.practicum.playlistmaker.features.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IAddTrackToPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistsUseCase
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.launch

class PlaylistBottomSheetViewModel(
    private val track: TrackModel,
    private val getPlaylistsUseCase: IGetPlaylistsUseCase,
    private val addTrackToPlaylistUseCase: IAddTrackToPlaylistUseCase
) : ViewModel() {

    private val _stateContent = MutableLiveData<BottomSheetContentState>()
    val stateContent: LiveData<BottomSheetContentState> = _stateContent
    private val _stateUi = MutableLiveData<BottomSheetUiState>(BottomSheetUiState.Hide)
    val stateUi: LiveData<BottomSheetUiState> = _stateUi

    init {
        viewModelScope.launch {
            getPlaylistsUseCase().collect { playlistModels ->
                renderContentState(BottomSheetContentState.Content(playlistModels))
            }
        }
    }

    fun addTrackToPlaylist(playlist: PlaylistModel) {
        if (isTrackAlreadyInPlaylist(playlist)) {
            renderContentState(BottomSheetContentState.AlreadyExists(playlist.title))
            return
        }
        viewModelScope.launch {
            val result = addTrackToPlaylistUseCase(playlist, track)
            onAddedResult(result)
        }
    }

    fun openBottomSheet() {
        renderUiState(BottomSheetUiState.Show)
    }

    private fun isTrackAlreadyInPlaylist(playlist: PlaylistModel) =
        track.trackId in playlist.idsTracks


    private fun onAddedResult(result: SaveResult) {
        when (result) {
            is SaveResult.Error -> {
                renderContentState(BottomSheetContentState.Error(result.errorMessage))
            }

            is SaveResult.Success -> {
                renderContentState(BottomSheetContentState.SuccessAdded(result.data))
            }
        }
    }

    private fun renderUiState(state: BottomSheetUiState) {
        _stateUi.value = state
    }

    private fun renderContentState(state: BottomSheetContentState){
        _stateContent.value = state
    }
}