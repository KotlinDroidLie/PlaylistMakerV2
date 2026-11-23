package com.practicum.playlistmaker.features.player.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.content.IntentCompat
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicService : Service() {
    private val _playbackState = MutableStateFlow<PlayerState>(PlayerState.Default(DEFAULT_PROGRESS))
    val playbackState = _playbackState.asStateFlow()
    private var timerJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private lateinit var mediaPlayer: MediaPlayer
   private lateinit var trackModel: TrackServiceModel
    companion object{
        private const val TIMER_CHANGE_DELAY = 300L
        const val DEFAULT_PROGRESS = "00:00"
        const val KEY_TRACK = "service_model_key"
    }
    private val binder = MusicServiceBinder()
    inner class MusicServiceBinder : Binder(){
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        trackModel = intent?.let {
            IntentCompat.getParcelableExtra(it, KEY_TRACK, TrackServiceModel::class.java)
        } ?: return binder
        preparedPlayer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        timerJob = null
    }
    fun playerControl() {
        when (_playbackState.value) {
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            is PlayerState.Playing -> pausePlayer()
            else -> {}
        }
    }
    private fun preparedPlayer() {
        with(mediaPlayer) {
            setDataSource(trackModel.audioPreviewUrl)
            prepareAsync()
            setOnPreparedListener {
                val state = PlayerState.Prepared(DEFAULT_PROGRESS)
                renderPlaybackState(state)
            }
            setOnCompletionListener {
                serviceScope.launch {
                    resetAfterCompletion()
                }
            }
        }
    }
    private fun releasePlayer(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private suspend fun resetAfterCompletion(){
        timerJob?.cancelAndJoin()
        resetTimer()
    }

    private fun resetTimer() {
        val state = PlayerState.Prepared(DEFAULT_PROGRESS)
        renderPlaybackState(state)
    }
    private fun pauseTimer() {
        timerJob?.cancel()
    }
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (mediaPlayer.isPlaying){
                delay(TIMER_CHANGE_DELAY)
                val state = PlayerState.Playing(getCurrentPlayerPosition())
                renderPlaybackState(state)
            }
        }
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
    private fun renderPlaybackState(state: PlayerState){
        _playbackState.value = state
    }
    private fun getCurrentPlayerPosition(): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }
}