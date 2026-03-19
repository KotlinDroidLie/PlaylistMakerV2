package com.practicum.playlistmaker.domain.api

import android.content.Intent

interface ShareAppUseCase {
    fun execute(): Intent
}