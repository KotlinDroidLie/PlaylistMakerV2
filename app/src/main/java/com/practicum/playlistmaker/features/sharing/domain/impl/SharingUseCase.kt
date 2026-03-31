package com.practicum.playlistmaker.features.sharing.domain.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.sharing.domain.model.EmailData
import com.practicum.playlistmaker.features.sharing.domain.api.IExternalNavigator
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingUseCase

class SharingUseCase(
    private val externalNavigator: IExternalNavigator,
    private val context: Context
    ): ISharingUseCase {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.link_share_app)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.my_mail),
            subject = context.getString(R.string.support_mail_title),
            text = context.getString(R.string.support_mail_text)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.link_user_agreement)
    }
}