package com.practicum.playlistmaker.features.media.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.media.data.api.IFileStorageClient
import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.media.data.db.entity.toEntity
import com.practicum.playlistmaker.features.media.data.db.entity.toModel
import com.practicum.playlistmaker.features.media.data.db.entity.toTracksInPlaylistsEntity
import com.practicum.playlistmaker.features.media.data.dto.ResponseStorage
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepo(
    private val appDataBase: AppDataBase,
    private val storageClient: IFileStorageClient
) : IPlaylistRepo {
    override suspend fun savePosterImage(sourceUri: String): SaveResult {
        return when(val response = storageClient.saveFile(sourceUri)){
            is ResponseStorage.Error -> {
                SaveResult.Error(
                    errorMessage = R.string.placeholder_text_error_storage
                )
            }
            is ResponseStorage.Success -> {
                SaveResult.Success(response.path)
            }
        }
    }

    override suspend fun insertPlaylist(playlist: PlaylistModel): SaveResult {
        return try{
            val entity = playlist.toEntity()
            appDataBase.playlistDao().insertPlaylist(entity)
            SaveResult.Success(playlist.title)
        } catch (e: Exception){
            SaveResult.Error(
                errorMessage =  R.string.placeholder_text_error_save_playlist_database
            )
        }

    }

    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return appDataBase.playlistDao()
            .getPlaylists()
            .map { entities -> entities.map { it.toModel() } }
    }

    override suspend fun insertTrackInPlaylist(
        track: TrackModel,
        playlist: PlaylistModel
    ): SaveResult {
        return try {
            val entityTrack = track.toTracksInPlaylistsEntity()
            appDataBase.tracksInPlaylistsDao().insertTrack(entityTrack)
            val updatedPlaylist = playlist.copy(
                totalTracks = playlist.totalTracks + 1,
                idsTracks = playlist.idsTracks + track.trackId
            )
            val entityPlaylist = updatedPlaylist.toEntity()
            appDataBase.playlistDao().insertPlaylist(entityPlaylist)
            SaveResult.Success(playlist.title)
        } catch (e: Exception){
            SaveResult.Error(
                errorMessage = R.string.placeholder_text_error_add_track_to_playlist_database
            )
        }
    }
}