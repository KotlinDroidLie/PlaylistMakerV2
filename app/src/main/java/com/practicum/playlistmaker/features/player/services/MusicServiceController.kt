package com.practicum.playlistmaker.features.player.services

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import com.practicum.playlistmaker.features.player.ui.view_model.IMusicServiceController
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerState
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicServiceController(private val appContext: Context): IMusicServiceController {
    private var musicService: MusicService? = null
    private var stateJob: Job? = null
    private val controllerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _playbackState = MutableStateFlow<PlayerState>(PlayerState.Default(MusicService.DEFAULT_PROGRESS))
    override val playbackState: StateFlow<PlayerState> = _playbackState.asStateFlow()
    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val service = service as MusicService.MusicServiceBinder
            musicService = service.getService()
            observePlaybackState()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
        }
    }

    override fun bindService(trackModel: TrackModel) {
        val serviceModel = trackModel.toServiceModel()
        val intent = Intent(appContext, MusicService::class.java).apply {
            putExtra(MusicService.KEY_TRACK, serviceModel)
        }
        appContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun unBindService() {
        stateJob?.cancel()
        appContext.unbindService(serviceConnection)
    }

    override fun playerControl() {
        musicService?.playerControl()
    }

    override fun hideForegroundNotification() {
        musicService?.hideForegroundNotification()
    }

    override fun showForegroundNotification() {
        musicService?.showForegroundNotification()
    }
    private fun observePlaybackState() {
        val service = musicService ?: return
        stateJob = controllerScope.launch {
            service.playbackState.collect { state ->
                _playbackState.value = state
            }
        }
    }
}