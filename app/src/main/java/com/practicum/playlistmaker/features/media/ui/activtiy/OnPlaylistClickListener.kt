package com.practicum.playlistmaker.features.media.ui.activtiy

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

fun interface OnPlaylistClickListener {
    fun navigate(playlistId: Int)
}