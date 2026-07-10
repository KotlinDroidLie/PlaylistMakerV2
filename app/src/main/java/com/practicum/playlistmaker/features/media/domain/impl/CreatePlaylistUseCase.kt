package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

class CreatePlaylistUseCase(
    private val repo: IPlaylistRepo
) : ICreatePlaylistUseCase {

    override suspend operator fun invoke(playlist: PlaylistModel): SaveResult {
        val savedUri = playlist.uri?.let { sourceUri ->
            val result = repo.savePosterImage(sourceUri)
            when(result){
                is SaveResult.Error -> return result
                is SaveResult.Success -> result.data
            }
        }
        val validPlaylist = playlist.copy(uri = savedUri)
        val saveResult = repo.insertPlaylist(validPlaylist)
        return saveResult
    }
}




