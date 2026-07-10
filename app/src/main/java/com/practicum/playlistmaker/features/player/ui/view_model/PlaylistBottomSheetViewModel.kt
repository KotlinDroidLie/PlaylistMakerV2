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

    private val _state = MutableLiveData<PlaylistBottomSheetState>(PlaylistBottomSheetState.Hide)
    val state: LiveData<PlaylistBottomSheetState> = _state

    fun addTrackToPlaylist(playlist: PlaylistModel) {
        if (isTrackAlreadyInPlaylist(playlist)) {
            renderState(PlaylistBottomSheetState.AlreadyExists(playlist.title))
            return
        }
        viewModelScope.launch {
            val result = addTrackToPlaylistUseCase(playlist, track)
            onAddResult(result)
        }
    }
    fun loadContent(){
        viewModelScope.launch {
            val result = getPlaylistsUseCase.once()
            renderState(PlaylistBottomSheetState.Content(result))
        }
    }

    fun openBottomSheet() {
        loadContent()
        renderState(PlaylistBottomSheetState.Show)
    }

    private fun isTrackAlreadyInPlaylist(playlist: PlaylistModel) =
        track.trackId in playlist.idsTracks


    private fun onAddResult(result: SaveResult) {
        when (result) {
            is SaveResult.Error -> {
                renderState(PlaylistBottomSheetState.Error(result.errorMessage))
            }

            is SaveResult.Success -> {
                renderState(PlaylistBottomSheetState.Added(result.data))
            }
        }
    }

    private fun renderState(state: PlaylistBottomSheetState) {
        _state.value = state
    }
}