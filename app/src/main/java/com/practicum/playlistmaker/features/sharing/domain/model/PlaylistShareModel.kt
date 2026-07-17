package com.practicum.playlistmaker.features.sharing.domain.model

import com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail.PlaylistUiModel

data class PlaylistShareModel(
    val title: String,
    val description: String?,
    val tracks: List<TrackShareModel>
)


fun PlaylistUiModel.toShareModel(tracks: List<TrackShareModel>): PlaylistShareModel{
    return PlaylistShareModel(
        title = this.title,
        description = this.description,
        tracks = tracks
    )
}
