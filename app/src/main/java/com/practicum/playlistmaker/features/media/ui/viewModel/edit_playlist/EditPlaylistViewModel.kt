package com.practicum.playlistmaker.features.media.ui.viewModel.edit_playlist

import com.practicum.playlistmaker.features.media.domain.api.ICreatePlaylistUseCase
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistViewModel

class EditPlaylistViewModel(
    createPlaylistUseCase: ICreatePlaylistUseCase,
    playlistId: Int
): CreatePlaylistViewModel(createPlaylistUseCase) {

}