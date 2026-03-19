package com.practicum.playlistmaker.domain.api.usecase

import android.widget.ImageView

interface LoadImageUseCase {
    fun execute(url: String, imageView: ImageView)
}