package com.practicum.playlistmaker.features.player.ui.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(model: TrackModel,formatTrackUseCase: IFormatTrackUseCase): ViewModel() {
    private val formattedTrack: PlayerUiModel
    private val mediaPlayer = MediaPlayer()
    init {
        formattedTrack = prepareFormattedTrack(model, formatTrackUseCase)
        preparedPlayer()
    }
    private fun prepareFormattedTrack(model: TrackModel, formatTrackUseCase: IFormatTrackUseCase): PlayerUiModel{
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

    private val mainHandler = Handler(Looper.getMainLooper())
    private val _playerState = MutableLiveData<Int>(STATE_DEFAULT)
    val playerState: LiveData<Int> = _playerState
    private val _timer = MutableLiveData<String>(TIMER_DEFAULT_VALUE)
    val timer: LiveData<String> = _timer
    private val _track = MutableLiveData<PlayerUiModel>(formattedTrack)
    val track: LiveData<PlayerUiModel> = _track


    companion object{
        private const val TIMER_DEFAULT_VALUE = "00:00"
        const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        fun getViewModelFactory(track: TrackModel, formatTrackUseCase: IFormatTrackUseCase): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track, formatTrackUseCase)
            }
        }
    }
    private fun preparedPlayer() {
        with(mediaPlayer){
            setDataSource(formattedTrack.audioPreviewUrl)
            prepareAsync()
            setOnPreparedListener {
                _playerState.postValue(STATE_PREPARED)
            }
            setOnCompletionListener {
                _playerState.postValue(STATE_PREPARED)
                resetTimer()
            }
        }
    }

    private val runnable = Runnable {
        if (playerState.value ==  STATE_PLAYING){
            startTimer()
        }
    }
    private fun startTimer(){
        val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        _timer.postValue(currentPosition)
        mainHandler.postDelayed(runnable, 500)
    }
    private fun pauseTimer(){
        mainHandler.removeCallbacks(runnable)
    }
    private fun resetTimer(){
        mainHandler.removeCallbacks(runnable)
        _timer.postValue(TIMER_DEFAULT_VALUE)
    }
    private fun startPlayer(){
        mediaPlayer.start()
        _playerState.postValue(STATE_PLAYING)
        startTimer()

    }
    private fun pausePlayer(){
        mediaPlayer.pause()
        _playerState.postValue(STATE_PAUSED)
        pauseTimer()
    }
    fun onPause(){
        pausePlayer()
    }

    fun playerControl(){
        when(playerState.value){
            STATE_PAUSED, STATE_PREPARED -> startPlayer()
            STATE_PLAYING -> pausePlayer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

}