package com.practicum.playlistmaker.features.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.Locale

class MediaController(private val mediaPlayer: MediaPlayer) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var playbackState = PlaybackState.DEFAULT
    private var playbackPositionCallback: ((String) -> Unit)? = null
    private var playbackCompletionCallback: (() -> Unit)? = null

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            playbackPositionCallback?.invoke(currentPosition)
            mainHandler.postDelayed(this, 500)
        }
    }

    fun setPlaybackPositionCallback(callback: (String) -> Unit) {
        playbackPositionCallback = callback
    }

    fun setPlaybackCompletionCallback(callback: () -> Unit) {
        playbackCompletionCallback = callback
        mediaPlayer.setOnCompletionListener {
            playbackState = PlaybackState.PREPARED
            pauseProgressUpdate()
            playbackPositionCallback?.invoke("00:00")
            callback()
        }
    }

    fun prepareMedia(audioUrl: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playbackState = PlaybackState.PREPARED
        }
    }

    fun play() {
        if (playbackState == PlaybackState.PREPARED || playbackState == PlaybackState.PAUSED) {
            mediaPlayer.start()
            playbackState = PlaybackState.PLAYING
            startProgressUpdate()
        }
    }

    fun pause() {
        if (playbackState == PlaybackState.PLAYING) {
            mediaPlayer.pause()
            playbackState = PlaybackState.PAUSED
            pauseProgressUpdate()
        }
    }

    fun release() {
        pauseProgressUpdate()
        mediaPlayer.release()
        playbackState = PlaybackState.DEFAULT
    }

    fun getCurrentState(): PlaybackState = playbackState

    private fun startProgressUpdate() {
        updateProgressRunnable.run()
    }

    private fun pauseProgressUpdate() {
        mainHandler.removeCallbacks(updateProgressRunnable)
    }
}

enum class PlaybackState {
    DEFAULT, PREPARED, PLAYING, PAUSED
}