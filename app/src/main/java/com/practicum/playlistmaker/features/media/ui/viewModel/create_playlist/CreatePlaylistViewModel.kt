package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val createPlaylistUseCase: ICreatePlaylistUseCase
): ViewModel() {
    private val _state = MutableLiveData<CreatePlaylistState>(CreatePlaylistState.Editing(PlaylistCreateUiModel()))
    val state: LiveData<CreatePlaylistState> = _state

    fun createPlaylist(){
        val currentState = state.value
        viewModelScope.launch {
            val playlist = when(currentState){
                is WithData -> currentState.playlist
                else -> return@launch
            }
            val domain = currentState.playlist.toDomain()
            renderState(CreatePlaylistState.Creating)
            val result = createPlaylistUseCase(domain)
            processResult(result, playlist)
        }
    }

    fun onTitleChanged(title: String){
        updatePlaylist { playlist ->
            playlist.copy(
                title = title,
                isButtonEnable = title.isNotBlank()
            )
        }
    }

    fun onDescriptionChanged(description: String){
        updatePlaylist { playlist ->
            playlist.copy(
                description = description
            )
        }
    }

    fun onUriChanged(uri: Uri){
       updatePlaylist { playlist ->
           playlist.copy(
               coverImagePath = uri.toString()
           )
       }
    }

    private fun updatePlaylist(transform: (PlaylistCreateUiModel) -> PlaylistCreateUiModel){
        val currentState = state.value
        if(!isModifiable(currentState)) return
        val validState = currentState as WithData
        val updatedPlaylist = transform(validState.playlist)
        renderState(CreatePlaylistState.Editing(updatedPlaylist))
    }

    private fun isModifiable(state: CreatePlaylistState?): Boolean{
        return when(state){
            is WithData -> true
            else -> false
        }
    }

    private fun renderState(state: CreatePlaylistState) {
        _state.value = state
    }

    private fun processResult(result: SaveResult, playlist: PlaylistCreateUiModel){
        when(result){
            is SaveResult.Error -> {
                renderState(CreatePlaylistState.Error(playlist,result.errorMessage))
            }
            is SaveResult.Success -> {
                renderState(CreatePlaylistState.Created(result.data))
            }
        }
    }
}