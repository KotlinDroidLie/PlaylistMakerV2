package com.practicum.playlistmaker.features.media.data.api

import com.practicum.playlistmaker.features.media.data.dto.ResponseStorage

interface IFileStorageClient{
    suspend fun saveFile(sourceUri: String): ResponseStorage
    suspend fun deleteFile(path: String): ResponseStorage
}