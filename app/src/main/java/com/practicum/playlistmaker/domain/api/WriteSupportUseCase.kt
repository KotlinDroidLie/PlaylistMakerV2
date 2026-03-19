package com.practicum.playlistmaker.domain.api

import android.content.Intent

interface WriteSupportUseCase {
    fun execute(): Intent
}