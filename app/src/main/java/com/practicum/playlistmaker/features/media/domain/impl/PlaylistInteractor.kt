package com.practicum.playlistmaker.features.media.domain.impl

import com.practicum.playlistmaker.features.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow
class PlaylistInteractor(
    private val repo: IPlaylistRepo
) : IPlaylistInteractor {

    override suspend fun createPlaylist(playlist: PlaylistModel): SaveResult {
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

    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return repo.getPlaylists()
    }
}




