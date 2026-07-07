package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

import android.net.Uri

data class PlaylistUiModel(
    val title: String = "",
    val description: String = "",
    val uri: Uri? = null,
    val isButtonEnable: Boolean = false
)