package com.practicum.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.usecase.ShareAppUseCase

class ShareAppUseCaseImpl(private val context: Context) : ShareAppUseCase {
    override fun execute(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_share_app))
        }
    }
}