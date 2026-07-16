package com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IFormatPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistTracksUseCase
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val getPlaylistTracks: IGetPlaylistTracksUseCase,
    private val getPlaylistByIdUseCase: IGetPlaylistByIdUseCase,
    private val formatPlaylistUseCase: IFormatPlaylistUseCase,
    private val playlistId: Int
): ViewModel() {
    private val _model = MutableLiveData<PlaylistUiModel>()
    val model: LiveData<PlaylistUiModel> = _model

    init {
        loadPlaylist()
    }

    private fun loadPlaylist(){
        viewModelScope.launch {
            val model = getPlaylistByIdUseCase(playlistId)
            getPlaylistTracks(model.idsTracks).collect { tracks ->
                val uiModel = prepareFormattedPlaylist(model, tracks)
                renderUiModel(uiModel)
            }
        }
    }



    private fun prepareFormattedPlaylist(
        model: PlaylistModel,
        tracks: List<TrackModel>,
    ): PlaylistUiModel {
        return PlaylistUiModel(
            id = model.id,
            title = model.title,
            description = model.description,
            uri = model.uri,
            totalTracks = tracks.size,
            totalDuration = formatPlaylistUseCase.getTracksDuration(tracks)
        )
    }

    private fun renderUiModel(uiModel: PlaylistUiModel) {
        _model.value = uiModel
    }
}