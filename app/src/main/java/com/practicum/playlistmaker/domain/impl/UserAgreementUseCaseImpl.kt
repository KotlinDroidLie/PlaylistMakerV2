package com.practicum.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.usecase.UserAgreementUseCase

class UserAgreementUseCaseImpl(private val context: Context) : UserAgreementUseCase {
    override fun execute(): Intent {
        return Intent().apply {
            action = Intent.ACTION_VIEW
            data = context.getString(R.string.link_user_agreement).toUri()
        }
    }
}