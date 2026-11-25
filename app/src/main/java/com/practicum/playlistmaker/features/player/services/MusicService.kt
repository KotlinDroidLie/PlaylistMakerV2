package com.practicum.playlistmaker.features.player.services

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.IntentCompat
import com.practicum.playlistmaker.R
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
import kotlin.time.Duration.Companion.milliseconds

class MusicService : Service() {
    private val _playbackState = MutableStateFlow<PlayerState>(PlayerState.Default(DEFAULT_PROGRESS))
    val playbackState = _playbackState.asStateFlow()
    private var timerJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private lateinit var mediaPlayer: MediaPlayer
   private lateinit var trackModel: TrackServiceModel
    companion object{
        const val SERVICE_NOTIFICATION_ID = 10
        private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
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
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannelCompat.Builder(
            NOTIFICATION_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(getString(R.string.notification_channel_name))
            .build()
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }
    private fun createServiceNotification(): Notification{
        val contentText = getString(R.string.foreground_notification_text, trackModel.artistName, trackModel.trackName)
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSilent(true)
            .setShowWhen(false)
            .build()
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
        stopForegroundNotification()
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
                delay(TIMER_CHANGE_DELAY.milliseconds)
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
    private fun startForegroundNotification(){
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
    }
    private fun stopForegroundNotification(){
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }
    private fun renderPlaybackState(state: PlayerState){
        _playbackState.value = state
    }
    private fun getCurrentPlayerPosition(): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    fun hideForegroundNotification() {
        if(isPlaying()){
            stopForegroundNotification()
        }
    }

    fun showForegroundNotification() {
        if(hasNotificationPermission() && isPlaying()){
            startForegroundNotification()
        }else{
            pausePlayer()
        }
    }

    private fun hasNotificationPermission(): Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val permissionProvider = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            if(permissionProvider == PackageManager.PERMISSION_DENIED ) return false
        }
        return true
    }
    private fun isPlaying(): Boolean{
        return _playbackState.value is PlayerState.Playing
    }
}