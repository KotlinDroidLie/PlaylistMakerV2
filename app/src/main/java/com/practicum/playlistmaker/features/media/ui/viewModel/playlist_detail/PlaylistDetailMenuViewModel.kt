package com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.player.ui.view_model.BottomSheetUiState
import kotlinx.coroutines.launch

class PlaylistDetailMenuViewModel(
    private val getPlaylistByIdUseCase: IGetPlaylistByIdUseCase,
    private val playlistId: Int
): ViewModel() {
    private val _stateUi = MutableLiveData<BottomSheetUiState>(BottomSheetUiState.Hide)
    val stateUi: LiveData<BottomSheetUiState> = _stateUi
    private val _content = MutableLiveData<PlaylistModel>()
    val content: LiveData<PlaylistModel> = _content
    fun openMenu() {
        loadContent()
        renderStateUi(BottomSheetUiState.Show)
    }

    private fun loadContent() {
        viewModelScope.launch {
            val playlist = getPlaylistByIdUseCase(playlistId)
            renderContent(playlist)
        }
    }
    private fun renderStateUi(state: BottomSheetUiState){
        _stateUi.value = state
    }

    private fun renderContent(playlist: PlaylistModel) {
       _content.value = playlist
    }
}