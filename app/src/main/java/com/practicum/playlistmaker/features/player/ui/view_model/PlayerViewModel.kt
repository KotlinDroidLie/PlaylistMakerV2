package com.practicum.playlistmaker.features.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteInteractor
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val model: TrackModel,
    formatTrackUseCase: IFormatTrackUseCase,
    private val musicServiceController: IMusicServiceController,
    private val favouriteInteractor: IFavouriteInteractor
) : ViewModel() {
    private var trackUiModel = prepareFormattedTrack(model, formatTrackUseCase)
    private val _trackState = MutableLiveData<PlayerUiModel>(trackUiModel)
    val trackState: LiveData<PlayerUiModel> = _trackState
    val playbackState: LiveData<PlayerState> = musicServiceController.playbackState.asLiveData()
    init {
        musicServiceController.bindService(model)
        observeFavouriteChanges()
    }

    private fun observeFavouriteChanges(){
        viewModelScope.launch {
            favouriteInteractor.getTracks().collect { favouriteTracks ->
                val isFavourite = _trackState.value?.id in favouriteTracks.map { it.trackId }
                val updatedTrack = _trackState.value?.copy(isFavourite = isFavourite)
                updatedTrack?.let {
                    renderUiModelState(it)
                }
            }
        }
    }

    private fun renderUiModelState(updatedTrack: PlayerUiModel){
        _trackState.postValue(updatedTrack)
    }

    private fun prepareFormattedTrack(
        model: TrackModel,
        formatTrackUseCase: IFormatTrackUseCase
    ): PlayerUiModel {
        return PlayerUiModel(
            id = model.trackId,
            trackName = model.trackName,
            artistName = model.artistName,
            albumName = model.albumName,
            releaseDate = formatTrackUseCase.getTrackYear(model.releaseDate),
            genre = model.genre,
            country = model.country,
            trackDuration = formatTrackUseCase.getTrackDuration(model.trackDuration),
            trackImage = formatTrackUseCase.getCoverArtwork(model.trackImage),
            audioPreviewUrl = model.audioPreviewUrl,
            isFavourite =  model.isFavourite
        )
    }

    fun playerControl() {
       musicServiceController.playerControl()
    }

    fun onFavoriteClicked(){
        viewModelScope.launch {
            val isFavourite = _trackState.value?.isFavourite ?: false
            if(isFavourite){
                favouriteInteractor.removeTrack(model.trackId)
            } else {
                favouriteInteractor.insertTrack(model)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceController.unBindService()
    }

    fun hideForegroundNotification() {
        musicServiceController.hideForegroundNotification()
    }

    fun showForegroundNotification() {
        musicServiceController.showForegroundNotification()
    }
}