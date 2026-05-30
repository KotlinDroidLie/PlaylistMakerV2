package com.practicum.playlistmaker.features.player.ui.view_model

sealed class PlayerState(var track: PlayerUiModel, var buttonIsEnable: Boolean, var buttonIsPlayIcon: Boolean, var progress: String){
    class Default(track: PlayerUiModel): PlayerState(track, false, true, "00:00")
    class Prepared(track: PlayerUiModel): PlayerState(track,true, true, "00:00")
    class Playing(track: PlayerUiModel,progress: String): PlayerState(track,true, false, progress)
    class Paused(track: PlayerUiModel,progress: String): PlayerState(track,true, true, progress)
}