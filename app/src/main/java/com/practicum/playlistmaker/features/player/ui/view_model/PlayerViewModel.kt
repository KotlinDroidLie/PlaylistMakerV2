package com.practicum.playlistmaker.features.player.ui.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    model: TrackModel,
    formatTrackUseCase: IFormatTrackUseCase,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {
    private val track = prepareFormattedTrack(model, formatTrackUseCase)
    private val _state = MutableLiveData<PlayerState>(
        PlayerState.Default(
            track = track,
            DEFAULT_PROGRESS
        )
    )
    val state: LiveData<PlayerState> = _state

    private var timerJob: Job? = null

    init {
        preparedPlayer()
    }

    private fun prepareFormattedTrack(
        model: TrackModel,
        formatTrackUseCase: IFormatTrackUseCase
    ): PlayerUiModel {
        return PlayerUiModel(
            trackName = model.trackName,
            artistName = model.artistName,
            albumName = model.albumName,
            releaseDate = formatTrackUseCase.getTrackYear(model.releaseDate),
            genre = model.genre,
            country = model.country,
            trackDuration = formatTrackUseCase.getTrackDuration(model.trackDuration),
            trackImage = formatTrackUseCase.getCoverArtwork(model.trackImage),
            audioPreviewUrl = model.audioPreviewUrl
        )
    }

    private fun preparedPlayer() {
        with(mediaPlayer) {
            setDataSource(_state.value?.track?.audioPreviewUrl)
            prepareAsync()
            setOnPreparedListener {
                _state.postValue(PlayerState.Prepared(track, DEFAULT_PROGRESS))
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
                _state.postValue(PlayerState.Playing(track,getCurrentPlayerPosition()))
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
        _state.postValue(PlayerState.Prepared(track, DEFAULT_PROGRESS))
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _state.postValue(PlayerState.Playing(track,getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        _state.postValue(PlayerState.Paused(track,getCurrentPlayerPosition()))
    }

    fun onPause() {
        pausePlayer()
    }

    fun playerControl() {
        when (_state.value) {
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            is PlayerState.Playing -> pausePlayer()
            else -> {}
        }
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