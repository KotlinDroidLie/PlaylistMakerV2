package com.practicum.playlistmaker.features.player.ui.activity

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

fun interface OnPlaylistClickListener {
    fun addTrackInPlaylist(playlist: PlaylistModel)
}