package com.practicum.playlistmaker.features.media.data.dto

sealed interface ResponseStorage {
    data class Success(val path: String): ResponseStorage
    data class Error(val exceptionMessage: String? = null): ResponseStorage
}