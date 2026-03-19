package com.practicum.playlistmaker.domain.impl

import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.domain.api.UserAgreementUseCase

class UserAgreementUseCaseImpl(private val link: String) : UserAgreementUseCase {
    override fun execute(): Intent {
        return Intent().apply {
            action = Intent.ACTION_VIEW
            data = link.toUri()
        }
    }
}