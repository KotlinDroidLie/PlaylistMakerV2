package com.practicum.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.usecase.WriteSupportUseCase

class WriteSupportUseCaseImpl(private val context: Context) : WriteSupportUseCase {
    override fun execute(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SENDTO
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.my_mail)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_mail_title))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_mail_text))
        }
    }
}