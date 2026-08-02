package com.practicum.playlistmaker.features.player.ui.view_model

sealed class PlayerState(val buttonIsEnable: Boolean, val progress: String){
    class Default(defaultProgress: String): PlayerState(false, defaultProgress)
    class Prepared(defaultProgress: String): PlayerState(true, defaultProgress)
    class Playing(progress: String): PlayerState(true, progress)
    class Paused(progress: String): PlayerState(true, progress)
}