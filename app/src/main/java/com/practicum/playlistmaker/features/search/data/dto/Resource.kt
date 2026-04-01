package com.practicum.playlistmaker.features.search.data.dto

import androidx.annotation.StringRes

sealed class Resource<T>(val data: T? = null, val message: Int? = null, val type: ErrorType? = null, val extraMessage: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(@StringRes message: Int, data: T? = null, type:ErrorType, extraMessage: String? = null): Resource<T>(data, message,type, extraMessage)
}

enum class ErrorType{
    NETWORK,
    GENERIC,
    EXCEPTION
}