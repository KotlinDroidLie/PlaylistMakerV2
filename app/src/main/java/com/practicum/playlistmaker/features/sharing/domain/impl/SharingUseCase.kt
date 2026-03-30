package com.practicum.playlistmaker.features.sharing.domain.impl

import com.practicum.playlistmaker.features.sharing.domain.model.EmailData
import com.practicum.playlistmaker.features.sharing.domain.api.IExternalNavigator
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingUseCase

class SharingUseCase(private val externalNavigator: IExternalNavigator): ISharingUseCase {
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
        return "https://practicum.yandex.ru/android-developer/"
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = "asdxasdx20022002@ya.ru",
            subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            text = "Спасибо разработчикам и разработчицам за крутое приложение!"
        )
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/ru/"
    }
}