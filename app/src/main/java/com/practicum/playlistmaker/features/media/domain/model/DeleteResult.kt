package com.practicum.playlistmaker.features.media.domain.model

sealed interface DeleteResult {
    object Success : DeleteResult
    data class Error(val errorMessage: Int) : DeleteResult
}