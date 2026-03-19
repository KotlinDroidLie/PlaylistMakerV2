package com.practicum.playlistmaker.domain.impl

import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.domain.api.ShareAppUseCase

class ShareAppUseCaseImpl(private val shareLink: String) : ShareAppUseCase {
    override fun execute(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareLink)
        }
    }
}