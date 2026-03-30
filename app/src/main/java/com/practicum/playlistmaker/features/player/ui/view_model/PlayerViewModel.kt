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
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val url: String): ViewModel() {
    private val mediaPlayer = MediaPlayer()
    private val mainHandler = Handler(Looper.getMainLooper())
    private val _playerState = MutableLiveData<Int>(STATE_DEFAULT)
    private val playerState: LiveData<Int> = _playerState
    private val _timer = MutableLiveData<String>(TIMER_DEFAULT_VALUE)
    private val timer: LiveData<String> = _timer
    companion object{
        private const val TIMER_DEFAULT_VALUE = "00:00"
        const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(url)
            }
        }
    }
    init {
        preparedPlayer()
    }
    private fun preparedPlayer() {
        with(mediaPlayer){
            setDataSource(url)
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