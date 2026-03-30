package com.practicum.playlistmaker.features.search.data.dto

sealed class Resource<T>(val data: T? = null, val message: String? = null, val type: ErrorType? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null, type:ErrorType): Resource<T>(data, message,type)
}

enum class ErrorType{
    NETWORK,
    GENERIC,
    EXCEPTION
}