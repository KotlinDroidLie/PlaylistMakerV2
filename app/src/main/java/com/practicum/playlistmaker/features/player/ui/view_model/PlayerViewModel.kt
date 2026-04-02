package com.practicum.playlistmaker.features.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    model: TrackModel,
    formatTrackUseCase: IFormatTrackUseCase,
    private val mediaPlayer: MediaPlayer,
    private val mainHandler: Handler
) : ViewModel() {
    private val formattedTrack = prepareFormattedTrack(model, formatTrackUseCase)
    private val _state = MutableLiveData<PlayerState>(
        PlayerState(
            track = formattedTrack,
            playerStatus = STATE_DEFAULT,
            timer = TIMER_DEFAULT_VALUE
        )
    )
    val state: LiveData<PlayerState> = _state

    private val runnable = Runnable {
        if (_state.value?.playerStatus == STATE_PLAYING) {
            startTimer()
        }
    }

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
                _state.value = _state.value?.copy(playerStatus = STATE_PREPARED)
            }
            setOnCompletionListener {
                _state.value = _state.value?.copy(playerStatus = STATE_PREPARED)
                resetTimer()
            }
        }
    }

    private fun startTimer() {
        val currentPosition =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        _state.value = _state.value?.copy(timer = currentPosition)
        mainHandler.postDelayed(runnable, 500)
    }

    private fun pauseTimer() {
        mainHandler.removeCallbacks(runnable)
    }

    private fun resetTimer() {
        mainHandler.removeCallbacks(runnable)
        _state.value = _state.value?.copy(timer = TIMER_DEFAULT_VALUE)
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _state.value = _state.value?.copy(playerStatus = STATE_PLAYING)
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        _state.value = _state.value?.copy(playerStatus = STATE_PAUSED)
        pauseTimer()
    }

    fun onPause() {
        pausePlayer()
    }

    fun playerControl() {
        when (_state.value?.playerStatus) {
            STATE_PAUSED, STATE_PREPARED -> startPlayer()
            STATE_PLAYING -> pausePlayer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    companion object {
        private const val TIMER_DEFAULT_VALUE = "00:00"
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}