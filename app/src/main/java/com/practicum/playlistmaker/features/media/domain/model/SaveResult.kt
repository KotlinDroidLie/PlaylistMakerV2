package com.practicum.playlistmaker.features.media.domain.model

sealed interface SaveResult{
    data class Success(val data: String) : SaveResult
    data class Error(val errorMessage: Int, val extraMessage: String? = null) : SaveResult
}