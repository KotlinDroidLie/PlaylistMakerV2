package com.practicum.playlistmaker.features.media.data.impl

import android.content.ContentResolver
import androidx.core.net.toUri
import com.practicum.playlistmaker.features.media.data.api.IFileStorageClient
import com.practicum.playlistmaker.features.media.data.dto.ResponseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileStorageClient(
    private val fileDir: File,
    private val contentResolver: ContentResolver,
    private val catalogName: String
) : IFileStorageClient {
    override suspend fun saveFile(sourceUri: String): ResponseStorage {
        return withContext(Dispatchers.IO){
            try {
                val dir = File(fileDir, catalogName)
                if(!dir.exists()){
                    dir.mkdirs()
                }
                val currentName = System.currentTimeMillis().toString()
                val file = File(dir,currentName)
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

    override suspend fun deleteFile(path: String): ResponseStorage {
        return withContext(Dispatchers.IO){
            try {
                val file = File(path)
                if(file.exists()){
                    file.delete()
                }
                ResponseStorage.Success(file.path)
            } catch (e: Exception){
                ResponseStorage.Error(e.message)
            }
        }
    }
}