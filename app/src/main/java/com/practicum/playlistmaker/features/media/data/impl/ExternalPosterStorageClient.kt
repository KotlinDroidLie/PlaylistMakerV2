package com.practicum.playlistmaker.features.media.data.impl

import android.content.ContentResolver
import androidx.core.net.toUri
import com.practicum.playlistmaker.features.media.data.api.IExternalStorageClient
import com.practicum.playlistmaker.features.media.data.dto.ResponseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExternalPosterStorageClient(
    private val externalFileDir: File,
    private val contentResolver: ContentResolver,
    private val catalogName: String
) : IExternalStorageClient {
    override suspend fun saveImage(sourceUri: String): ResponseStorage {
        return withContext(Dispatchers.IO){
            try {
                val posterDir = File(externalFileDir, catalogName)
                if(!posterDir.exists()){
                    posterDir.mkdirs()
                }
                val currentName = System.currentTimeMillis().toString()
                val file = File(posterDir,currentName)
                val uri = sourceUri.toUri()
                contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IOException()
                ResponseStorage.Success(file.absolutePath)
            } catch (e: Exception){
                ResponseStorage.Error(e.message)
            }
        }
    }
}