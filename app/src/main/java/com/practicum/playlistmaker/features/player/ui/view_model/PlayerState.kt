package com.practicum.playlistmaker.features.player.ui.view_model

sealed class PlayerState(val buttonIsEnable: Boolean, val buttonIsPlayIcon: Boolean, val progress: String){
    class Default(defaultProgress: String): PlayerState(false, true, defaultProgress)
    class Prepared(defaultProgress: String): PlayerState(true, true, defaultProgress)
    class Playing(progress: String): PlayerState(true, false, progress)
    class Paused(progress: String): PlayerState(true, true, progress)
}