package com.practicum.playlistmaker.features.media.ui.activtiy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavouriteBinding
import com.practicum.playlistmaker.features.media.ui.viewModel.FavouriteTracksState
import com.practicum.playlistmaker.features.media.ui.viewModel.FavouriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment(): Fragment() {
    private val viewModel: FavouriteTracksViewModel by viewModel()
    private var _binding: FragmentFavouriteBinding? = null
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
        viewModel.state.observe(viewLifecycleOwner){
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun render(state: FavouriteTracksState){
        when(state){
            is FavouriteTracksState.Content -> showContent()
            FavouriteTracksState.Empty -> showEmptyMessage()
        }
    }
    private fun showContent(){

    }

    private fun showEmptyMessage(){
        binding.apply {
            rvFavouriteMedia.isVisible = false
            viewMediaFavouriteStatusIsEmpty.root.isVisible = true
        }
    }
    companion object{
        fun newInstance() = FavouriteTracksFragment()
    }
}