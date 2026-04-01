package com.practicum.playlistmaker.features.sharing.domain.api

import com.practicum.playlistmaker.features.sharing.domain.model.EmailData

interface IExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link:String)
    fun openEmail(emailData: EmailData)
}