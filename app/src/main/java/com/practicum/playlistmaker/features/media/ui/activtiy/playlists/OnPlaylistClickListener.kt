package com.practicum.playlistmaker.features.media.ui.activtiy.playlists

fun interface OnPlaylistClickListener {
    fun navigate(playlistId: Int)
}