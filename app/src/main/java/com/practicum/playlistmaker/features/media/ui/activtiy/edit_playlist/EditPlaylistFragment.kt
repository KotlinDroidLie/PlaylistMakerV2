package com.practicum.playlistmaker.features.media.ui.activtiy.edit_playlist

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.media.ui.activtiy.create_playlist.CreatePlaylistFragment
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.PlaylistCreateUiModel
import com.practicum.playlistmaker.features.media.ui.viewModel.edit_playlist.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class EditPlaylistFragment: CreatePlaylistFragment() {
    override val viewModel: EditPlaylistViewModel by viewModel {
        parametersOf(requireArguments().getInt(ARGS_EDIT_PLAYLIST))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreatePlaylist.text = getString(R.string.edit_playlist_fragment_save_button)
        binding.btnCreatePlaylistBack.title = getString(R.string.edit_playlist_fragment_title)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
        binding.btnCreatePlaylist.setOnClickListener {
           viewModel.updatePlaylist()
        }
    }

    override fun handleState(state: CreatePlaylistState) {
        when(state){
            is CreatePlaylistState.SetupEditMode ->{
                fillData(state.playlist)
            }
            CreatePlaylistState.Saved -> {
                findNavController().navigateUp()
            }
            CreatePlaylistState.Saving ->{
                enableInput(false)
            }
            else -> super.handleState(state)
        }
    }
    private fun fillData(playlist: PlaylistCreateUiModel){
        binding.etTitle.setText(playlist.title)
        binding.etDescription.setText(playlist.description)
        playlist.coverImagePath?.let {
            setPoster(Uri.fromFile(File(it)))
        }
    }
    companion object{
        private const val ARGS_EDIT_PLAYLIST = "edit_playlist"
        fun createArgs(playlistId: Int) = Bundle().apply {
            putInt(ARGS_EDIT_PLAYLIST, playlistId)
        }
    }
}