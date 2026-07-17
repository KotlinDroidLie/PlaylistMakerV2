package com.practicum.playlistmaker.features.media.ui.activtiy.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouriteBinding
import com.practicum.playlistmaker.features.media.ui.viewModel.favourite.FavouriteTracksState
import com.practicum.playlistmaker.features.media.ui.viewModel.favourite.FavouriteTracksViewModel
import com.practicum.playlistmaker.features.player.ui.activity.AudioPlayerFragment
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.ui.activtiy.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment(): Fragment() {
    private val viewModel: FavouriteTracksViewModel by viewModel()
    private var _binding: FragmentFavouriteBinding? = null
    private lateinit var adapter: TrackAdapter
    private var isClickAllowed = true
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TrackAdapter(
            onTrackClickListener = { track ->
                openAudioPlayer(track)
            }
        )
        binding.rvFavouriteMedia.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFavouriteMedia.adapter = adapter
        viewModel.state.observe(viewLifecycleOwner){
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isClickAllowed = true
    }
    private fun openAudioPlayer(track: TrackModel) {
        if (clickDebounce()){
            findNavController().navigate(
                R.id.action_mediaFragment_to_audioPlayerFragment,
                AudioPlayerFragment.Companion.createARgs(track)
            )
        }
    }
    private fun render(state: FavouriteTracksState){
        when(state){
            is FavouriteTracksState.Content -> showContent(state.favouriteTracks)
            FavouriteTracksState.Empty -> showEmptyMessage()
        }
    }
    private fun showContent(favouriteTracks: List<TrackModel>){
        binding.apply {
            viewMediaFavouriteStatusIsEmpty.root.isVisible = false
            rvFavouriteMedia.isVisible = true
        }
        updateFavouriteList(favouriteTracks)
    }

    private fun updateFavouriteList(favouriteTracks: List<TrackModel>) {
        adapter.trackList.clear()
        adapter.trackList.addAll(favouriteTracks)
        adapter.notifyDataSetChanged()
    }


    private fun showEmptyMessage(){
        binding.apply {
            rvFavouriteMedia.isVisible = false
            viewMediaFavouriteStatusIsEmpty.root.isVisible = true
        }
    }
    private fun clickDebounce(): Boolean{
        val current = isClickAllowed
        if (current){
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
    companion object{
        fun newInstance() = FavouriteTracksFragment()
        private const val CLICK_DELAY = 1000L

    }
}