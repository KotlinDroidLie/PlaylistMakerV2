package com.practicum.playlistmaker.domain.api.usecase

import android.content.Intent

interface ShareAppUseCase {
    fun execute(): Intent
}