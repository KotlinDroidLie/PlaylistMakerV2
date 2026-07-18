package com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IDeletePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.model.DeleteResult
import com.practicum.playlistmaker.features.player.ui.view_model.BottomSheetUiState
import kotlinx.coroutines.launch

class PlaylistDetailMenuViewModel(
    private val getPlaylistByIdUseCase: IGetPlaylistByIdUseCase,
    private val deletePlaylistUseCase: IDeletePlaylistUseCase,
    private val playlistId: Int
): ViewModel() {
    private val _stateUi = MutableLiveData<BottomSheetUiState>(BottomSheetUiState.Hide)
    val stateUi: LiveData<BottomSheetUiState> = _stateUi
    private val _stateContent = MutableLiveData<PlaylistDetailMenuState>()
    val stateContent: LiveData<PlaylistDetailMenuState> = _stateContent
    fun openMenu() {
        loadContent()
        renderStateUi(BottomSheetUiState.Show)
    }

    private fun loadContent() {
        viewModelScope.launch {
            val playlist = getPlaylistByIdUseCase(playlistId)
            renderContent(PlaylistDetailMenuState.Content(playlist))
        }
    }
    private fun renderStateUi(state: BottomSheetUiState){
        _stateUi.value = state
    }

    private fun renderContent(playlist: PlaylistDetailMenuState) {
       _stateContent.value = playlist
    }
    fun deletePlaylist() {
        viewModelScope.launch {
            val result = deletePlaylistUseCase(playlistId)
            processDeleteResult(result)
        }
    }

    private fun processDeleteResult(result: DeleteResult) {
        when(result){
            is DeleteResult.Error ->{
                renderContent(PlaylistDetailMenuState.DeleteError(result.errorMessage))
            }
            DeleteResult.Success ->{
                renderContent(PlaylistDetailMenuState.DeleteSuccess)
            }
        }
    }
}