package com.practicum.playlistmaker.features.media.domain.api

import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.search.domain.model.TrackModel

interface IAddTrackToPlaylistUseCase {
    suspend operator fun invoke(playlist: PlaylistModel, track: TrackModel): SaveResult
}