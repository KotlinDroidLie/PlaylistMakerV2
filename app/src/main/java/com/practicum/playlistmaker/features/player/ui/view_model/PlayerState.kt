package com.practicum.playlistmaker.features.player.ui.view_model

sealed class PlayerState(val track: PlayerUiModel, val buttonIsEnable: Boolean, val buttonIsPlayIcon: Boolean, val progress: String){
    class Default(track: PlayerUiModel, defaultProgress: String): PlayerState(track, false, true, defaultProgress)
    class Prepared(track: PlayerUiModel, defaultProgress: String): PlayerState(track,true, true, defaultProgress)
    class Playing(track: PlayerUiModel,progress: String): PlayerState(track,true, false, progress)
    class Paused(track: PlayerUiModel,progress: String): PlayerState(track,true, true, progress)
}