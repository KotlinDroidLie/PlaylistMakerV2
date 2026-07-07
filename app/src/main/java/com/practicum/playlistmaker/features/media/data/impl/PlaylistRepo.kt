package com.practicum.playlistmaker.features.media.data.impl

import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.media.data.db.entity.toEntity
import com.practicum.playlistmaker.features.media.data.db.entity.toModel
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepo(
    private val appDataBase: AppDataBase
) : IPlaylistRepo {
    override suspend fun insertPlaylist(playlist: PlaylistModel) {
        val entity = playlist.toEntity()
        appDataBase.playlistDao().insertPlaylist(entity)
    }

    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return appDataBase.playlistDao()
            .getPlaylists()
            .map { entities -> entities.map { it.toModel() } }
    }
}