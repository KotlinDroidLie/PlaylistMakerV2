package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IAddTrackToPlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.search.domain.model.TrackModel

class AddTrackToPlaylistUseCase(
    private val repo: IPlaylistRepo
): IAddTrackToPlaylistUseCase {
    override suspend fun invoke(playlist: PlaylistModel, track: TrackModel): SaveResult {
        return repo.insertTrackInPlaylist(track,playlist)
    }
}