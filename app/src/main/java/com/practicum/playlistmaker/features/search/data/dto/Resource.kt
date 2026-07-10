package com.practicum.playlistmaker.features.search.data.dto

import androidx.annotation.StringRes

sealed interface Resource<T> {
    class Success<T>(val data: T): Resource<T>
    class Error<T>(@param:StringRes val message: Int, val data: T? = null, val type:ErrorType, val extraMessage: String? = null): Resource<T>
}

enum class ErrorType{
    NETWORK,
    GENERIC,
}