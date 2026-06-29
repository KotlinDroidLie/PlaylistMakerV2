package com.practicum.playlistmaker.features.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteInteractor
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val model: TrackModel,
    formatTrackUseCase: IFormatTrackUseCase,
    private val mediaPlayer: MediaPlayer,
    private val favouriteInteractor: IFavouriteInteractor
) : ViewModel() {
    private var trackUiModel = prepareFormattedTrack(model, formatTrackUseCase)
    private val _trackState = MutableLiveData<PlayerUiModel>(trackUiModel)
    val trackState: LiveData<PlayerUiModel> = _trackState
    private val _playbackState = MutableLiveData<PlayerState>(
        PlayerState.Default(
            DEFAULT_PROGRESS
        )
    )
    val playbackState: LiveData<PlayerState> = _playbackState

    private var timerJob: Job? = null

    init {
        preparedPlayer()
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

    private fun preparedPlayer() {
        with(mediaPlayer) {
            setDataSource(trackUiModel.audioPreviewUrl)
            prepareAsync()
            setOnPreparedListener {
                val state = PlayerState.Prepared(DEFAULT_PROGRESS)
                renderPlaybackState(state)
            }
            setOnCompletionListener {
                viewModelScope.launch {
                    resetAfterCompletion()
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying){
                delay(TIMER_CHANGE_DELAY)
                val state = PlayerState.Playing(getCurrentPlayerPosition())
                renderPlaybackState(state)
            }
        }
    }

    private fun getCurrentPlayerPosition(): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private suspend fun resetAfterCompletion(){
        timerJob?.cancelAndJoin()
        resetTimer()
    }

    private fun resetTimer() {
        val state = PlayerState.Prepared(DEFAULT_PROGRESS)
        renderPlaybackState(state)
    }

    private fun startPlayer() {
        mediaPlayer.start()
        val state = PlayerState.Playing(getCurrentPlayerPosition())
        renderPlaybackState(state)
        startTimer()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        val state = PlayerState.Paused(getCurrentPlayerPosition())
        renderPlaybackState(state)
    }

    fun onPause() {
        pausePlayer()
    }

    fun playerControl() {
        when (_playbackState.value) {
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            is PlayerState.Playing -> pausePlayer()
            else -> {}
        }
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
    private fun renderPlaybackState(state: PlayerState){
        _playbackState.postValue(state)
    }
    private fun releasePlayer(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    companion object{
        private const val TIMER_CHANGE_DELAY = 300L
        private const val DEFAULT_PROGRESS = "00:00"
    }
}