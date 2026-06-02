package com.practicum.playlistmaker.features.player.ui.view_model

import com.practicum.playlistmaker.R

sealed class PlayerState(var track: PlayerUiModel, var buttonIsEnable: Boolean, var buttonIsPlayIcon: Boolean, var progress: String){
        class Default(track: PlayerUiModel): PlayerState(track, false, true, R.string.defaultTimerPosition.toString())
    class Prepared(track: PlayerUiModel): PlayerState(track,true, true, R.string.defaultTimerPosition.toString())
    class Playing(track: PlayerUiModel,progress: String): PlayerState(track,true, false, progress)
    class Paused(track: PlayerUiModel,progress: String): PlayerState(track,true, true, progress)
}