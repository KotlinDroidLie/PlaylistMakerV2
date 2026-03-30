package com.practicum.playlistmaker.features.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.features.sharing.domain.model.EmailData
import com.practicum.playlistmaker.features.sharing.domain.api.IExternalNavigator

class ExternalNavigator(private val context: Context): IExternalNavigator {
    override fun shareLink(link: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
        }
        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val openIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = link.toUri()
        }
        context.startActivity(openIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val emailIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
        }
        context.startActivity(emailIntent)
    }
}