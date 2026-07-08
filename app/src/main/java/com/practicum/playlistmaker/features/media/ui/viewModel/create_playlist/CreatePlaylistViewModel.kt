package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: IPlaylistInteractor
): ViewModel() {
    private val _state = MutableLiveData<CreatePlaylistState>(CreatePlaylistState.Editing(PlaylistUiModel()))
    val state: LiveData<CreatePlaylistState> = _state

    fun createPlaylist(){
        val current = _state.value as? CreatePlaylistState.Editing ?: return
        val domain = current.playlist.toDomain()
        renderState(CreatePlaylistState.Creating)
        viewModelScope.launch {
            val result = playlistInteractor.createPlaylist(domain)
            processResult(result)
        }
    }

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
            coverImagePath = uri.toString()
        )
        val updatedState = CreatePlaylistState.Editing(updatedPlaylist)
        renderState(updatedState)
    }

    private fun renderState(state: CreatePlaylistState) {
        _state.postValue(state)
    }

    private fun processResult(result: SaveResult){
        when(result){
            is SaveResult.Error -> {
                Log.d("CreatePlaylistError","${result.extraMessage}")
            }
            is SaveResult.Success -> {
                renderState(CreatePlaylistState.Created(result.data))
            }
        }
    }
}