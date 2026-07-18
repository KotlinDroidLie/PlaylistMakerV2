package com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IFormatPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistByIdUseCase
import com.practicum.playlistmaker.features.media.domain.api.IGetPlaylistTracksUseCase
import com.practicum.playlistmaker.features.media.domain.api.IRemoveTrackUseCase
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingInteractor
import com.practicum.playlistmaker.features.sharing.domain.model.toShareModel
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val removeTrackUseCase: IRemoveTrackUseCase,
    private val getPlaylistTracks: IGetPlaylistTracksUseCase,
    private val getPlaylistByIdUseCase: IGetPlaylistByIdUseCase,
    private val formatPlaylistUseCase: IFormatPlaylistUseCase,
    private val sharingInteractor: ISharingInteractor,
    private val playlistId: Int
): ViewModel() {
    private val _playlist = MutableLiveData<PlaylistUiModel>()
    val playlist: LiveData<PlaylistUiModel> = _playlist
    private val _tracks = MutableLiveData<List<TrackModel>>()
    val tracks: LiveData<List<TrackModel>> = _tracks

    init {
        loadPlaylist()
    }
    fun removeTrack(trackId: Int){
        viewModelScope.launch {
            val playlistId = playlist.value?.id ?: return@launch
            removeTrackUseCase(
                trackId= trackId,
                playlistId = playlistId
            )
            loadPlaylist()
        }
    }

    fun sharePlaylist(){
        val tracksShareModel = tracks.value?.map { it.toShareModel() } ?: emptyList()
        val playlistShareModel = playlist.value?.toShareModel(tracksShareModel) ?: return
        sharingInteractor.sharePlaylist(playlistShareModel)
    }

    fun loadPlaylist(){
        viewModelScope.launch {
            val model = getPlaylistByIdUseCase(playlistId)
            getPlaylistTracks(model.idsTracks).collect { tracks ->
                val uiModel = prepareFormattedPlaylist(model, tracks)
                renderTracks(tracks)
                renderPlaylist(uiModel)
            }
        }
    }

    private fun renderTracks(tracks: List<TrackModel>) {
        _tracks.value = tracks
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

    private fun renderPlaylist(uiModel: PlaylistUiModel) {
        _playlist.value = uiModel
    }
}