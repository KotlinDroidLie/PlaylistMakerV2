package com.practicum.playlistmaker.features.sharing.domain.api

import com.practicum.playlistmaker.features.sharing.domain.model.PlaylistShareModel

interface ISharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(playlist: PlaylistShareModel)
}