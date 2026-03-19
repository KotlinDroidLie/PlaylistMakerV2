package com.practicum.playlistmaker.domain.impl

import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.domain.api.usecase.WriteSupportUseCase

class WriteSupportUseCaseImpl(
    private val email: String,
    private val subject: String,
    private val text: String
) : WriteSupportUseCase {
    override fun execute(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SENDTO
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
    }
}