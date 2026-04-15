package com.practicum.playlistmaker.features.media.ui.activtiy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.features.media.ui.viewModel.PlaylistState
import com.practicum.playlistmaker.features.media.ui.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlaylistFragment(): Fragment() {
    private val viewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner){
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistState){
        when(state){
            is PlaylistState.Content -> showContent()
            PlaylistState.Empty -> showEmptyMessage()
        }
    }

    private fun showContent(){

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