package com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

fun PlaylistCreateUiModel.toDomain(): PlaylistModel{
    return PlaylistModel(
        title = this.title,
        description = this.description.ifBlank { null },
        uri = this.coverImagePath,
    )
}

fun PlaylistModel.toCreateUiModel(): PlaylistCreateUiModel{
    return PlaylistCreateUiModel(
        title = this.title,
        description = this.description ?: "",
        coverImagePath = this.uri,
        isButtonEnable = true
    )
}