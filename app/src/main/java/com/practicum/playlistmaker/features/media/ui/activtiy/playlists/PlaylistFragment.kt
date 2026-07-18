package com.practicum.playlistmaker.features.media.ui.activtiy.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.media.ui.activtiy.playlist_detail.PlaylistDetailFragment
import com.practicum.playlistmaker.features.media.ui.viewModel.playlists.PlaylistViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.playlists.PlaylistsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment(): Fragment() {
    private val viewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistGridAdapter

    private val onPlaylistClickListener = OnPlaylistClickListener { playlistId ->
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistDetailFragment,
            PlaylistDetailFragment.Companion.createARgs(playlistId)
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlaylistMedia.layoutManager = GridLayoutManager(
            requireContext(), 2,
            GridLayoutManager.VERTICAL,
            false
        )

        adapter = PlaylistGridAdapter(onPlaylistClickListener)
        binding.rvPlaylistMedia.adapter = adapter

        binding.btnAddNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaFragment_to_createPlaylistFragment,
                )
        }


        viewModel.state.observe(viewLifecycleOwner){
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsState){
        when(state){
            is PlaylistsState.Content -> showContent(state.playlists)
            PlaylistsState.Empty -> showEmptyMessage()
        }
    }

    private fun showContent(playlists: List<PlaylistModel>){
        binding.apply {
            rvPlaylistMedia.isVisible = true
            viewMediaPlaylistStatusIsEmpty.root.isVisible = false
        }
        updateContent(playlists)
    }

    private fun updateContent(playlists: List<PlaylistModel>) {
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyMessage(){
        binding.apply {
            rvPlaylistMedia.isVisible = false
            viewMediaPlaylistStatusIsEmpty.root.isVisible = true
        }
    }

    companion object{
        fun newInstance() = PlaylistFragment()
    }
}