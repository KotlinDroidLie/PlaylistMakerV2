package com.practicum.playlistmaker.features.sharing.domain.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import com.practicum.playlistmaker.features.sharing.domain.model.EmailData
import com.practicum.playlistmaker.features.sharing.domain.api.IExternalNavigator
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingInteractor
import com.practicum.playlistmaker.features.sharing.domain.model.PlaylistShareModel

class SharingInteractor(
    private val externalNavigator: IExternalNavigator,
    private val formatPlaylistUseCase: IFormatTrackUseCase,
    private val context: Context
    ): ISharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun sharePlaylist(playlist: PlaylistShareModel) {
        val message = getMessageSharePlaylist(playlist)
        externalNavigator.sharePlaylist(message)
    }
    private fun getMessageSharePlaylist(playlist: PlaylistShareModel): String{
        val sb = StringBuilder()
        sb.appendLine(playlist.title)
        if(!playlist.description.isNullOrEmpty()){
            sb.appendLine(playlist.description)
        }
        sb.appendLine(
            context.resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracks.size,
                playlist.tracks.size
            )
        )
        sb.appendLine()
        playlist.tracks.forEachIndexed { index, track ->
            val duration = formatPlaylistUseCase.getTrackDuration(track.trackDuration)
            val number = index + 1
            sb.appendLine("${number}. ${track.artistName} - ${track.trackName} ($duration)")
        }
        return sb.toString()
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