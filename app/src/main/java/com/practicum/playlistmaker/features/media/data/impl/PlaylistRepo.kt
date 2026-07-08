package com.practicum.playlistmaker.features.media.data.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.media.data.api.IExternalStorageClient
import com.practicum.playlistmaker.features.media.data.db.AppDataBase
import com.practicum.playlistmaker.features.media.data.db.entity.toEntity
import com.practicum.playlistmaker.features.media.data.db.entity.toModel
import com.practicum.playlistmaker.features.media.data.dto.ResponseStorage
import com.practicum.playlistmaker.features.media.domain.api.IPlaylistRepo
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.domain.model.SaveResult
import com.practicum.playlistmaker.features.search.data.dto.ErrorType
import com.practicum.playlistmaker.features.search.data.dto.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepo(
    private val appDataBase: AppDataBase,
    private val storageClient: IExternalStorageClient
) : IPlaylistRepo {
    override suspend fun savePosterImage(sourceUri: String): Resource<String> {
        return when(val response = storageClient.saveImage(sourceUri)){
            is ResponseStorage.Error -> {
                Resource.Error(
                    message = R.string.placeholder_text_error_storage,
                    type = ErrorType.STORAGE
                )
            }
            is ResponseStorage.Success -> {
                Resource.Success(response.path)
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
                errorMessage = R.string.placeholder_text_error_database
            )
        }

    }

    override fun getPlaylists(): Flow<List<PlaylistModel>> {
        return appDataBase.playlistDao()
            .getPlaylists()
            .map { entities -> entities.map { it.toModel() } }
    }
}