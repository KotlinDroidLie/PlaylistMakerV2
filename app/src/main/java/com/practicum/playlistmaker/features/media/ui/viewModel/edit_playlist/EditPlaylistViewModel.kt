package com.practicum.playlistmaker.features.media.ui.viewModel.edit_playlist

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.PlaylistCreateUiModel
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.WithData
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.toCreateUiModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val getPlaylistByIdUseCase: IGetPlaylistByIdUseCase,
    private val createPlaylistUseCase: ICreatePlaylistUseCase,
    private val playlistId: Int
): CreatePlaylistViewModel(createPlaylistUseCase) {
    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch {
            val playlist = getPlaylistByIdUseCase(playlistId)
            renderState(CreatePlaylistState.SetupEditMode(playlist.toCreateUiModel()))
        }
    }

    fun updatePlaylist() {
        val currentState = state.value
        viewModelScope.launch {
            val playlist = when(currentState){
                is WithData -> currentState.playlist
                else -> return@launch
            }

            val validUriString = playlist.coverImagePath?.let { path ->
                when {
                    path.startsWith("file://") -> path
                    path.startsWith("content://") -> path
                    else -> "file://$path"
                }
            }

            val originalPlaylist = getPlaylistByIdUseCase(playlistId)

            val updatedPlaylist = originalPlaylist.copy(
                title = playlist.title,
                description = playlist.description,
                uri = validUriString
            )

            renderState(CreatePlaylistState.Saving)
            val result = createPlaylistUseCase(updatedPlaylist)
            processResult(result, playlist)
        }
    }

    override fun processResult(result: SaveResult, playlist: PlaylistCreateUiModel) {
        when(result){
            is SaveResult.Error -> {
                renderState(CreatePlaylistState.Error(playlist,result.errorMessage))
            }
            is SaveResult.Success -> {
                renderState(CreatePlaylistState.Saved)
            }
        }
    }
}