package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.features.media.domain.api.IAddPlaylistUseCase

class CreatePlaylistViewModel(
    private val playlistInteractor: IAddPlaylistUseCase
): ViewModel() {
    private val _state = MutableLiveData<CreatePlaylistState>(CreatePlaylistState.Editing(PlaylistUiModel()))
    val state: LiveData<CreatePlaylistState> = _state

    fun onTitleChanged(title: String){
        val current = _state.value as? CreatePlaylistState.Editing ?: return
        val updatedPlaylist = current.playlist.copy(
            title = title,
            isButtonEnable = title.isNotBlank()
        )
        val updatedState = CreatePlaylistState.Editing(updatedPlaylist)
        renderState(updatedState)
    }

    fun onDescriptionChanged(description: String){
        val current = _state.value as? CreatePlaylistState.Editing ?: return
        val updatedPlaylist = current.playlist.copy(
            description = description
        )
        val updatedState = CreatePlaylistState.Editing(updatedPlaylist)
        renderState(updatedState)
    }

    fun onUriChanged(uri: Uri){
        val current = _state.value as? CreatePlaylistState.Editing ?: return
        val updatedPlaylist = current.playlist.copy(
            uri = uri
        )
        val updatedState = CreatePlaylistState.Editing(updatedPlaylist)
        renderState(updatedState)
    }

    private fun renderState(state: CreatePlaylistState) {
        _state.postValue(state)
    }
}