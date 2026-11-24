package com.practicum.playlistmaker.features.player.ui.view_model

import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.StateFlow

interface IMusicServiceController {
    fun bindService(trackModel: TrackModel)
    fun unBindService()
    fun playerControl()
    fun hideForegroundNotification()
    fun showForegroundNotification()

    val playbackState: StateFlow<PlayerState>
}