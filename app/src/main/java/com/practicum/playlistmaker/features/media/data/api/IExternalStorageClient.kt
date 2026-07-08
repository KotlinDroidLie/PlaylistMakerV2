package com.practicum.playlistmaker.features.media.data.api

import com.practicum.playlistmaker.features.media.data.dto.ResponseStorage

interface IExternalStorageClient {
    suspend fun saveImage(sourceUri: String): ResponseStorage
}