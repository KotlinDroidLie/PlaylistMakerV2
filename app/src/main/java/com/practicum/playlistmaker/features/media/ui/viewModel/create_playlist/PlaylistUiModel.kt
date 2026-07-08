package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

data class PlaylistUiModel(
    val title: String = "",
    val description: String = "",
    val coverImagePath: String? = null,
    val isButtonEnable: Boolean = false
)


