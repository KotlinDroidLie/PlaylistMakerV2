package com.practicum.playlistmaker.features.media.ui.activtiy

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAboutPlaylistBinding
import com.practicum.playlistmaker.databinding.PlaylistBottomSheetBinding
import com.practicum.playlistmaker.features.main.BottomNavigationOwner
import com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail.PlaylistDetailViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.playlist_detail.PlaylistUiModel
import com.practicum.playlistmaker.features.player.ui.activity.AudioPlayerFragment
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.ui.activtiy.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailFragment: Fragment() {
    private var _binding: FragmentAboutPlaylistBinding? = null
    private val binding get() = _binding!!
    private var _bottomSheetBinding: PlaylistBottomSheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!
    private var isClickAllowed = true
    private val viewModel: PlaylistDetailViewModel by viewModel {
        parametersOf(requireArguments().getInt(ARGS_PLAYLIST))
    }

    private val cornerRadius by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            requireContext().resources.displayMetrics
        ).toInt()
    }
    private lateinit var bottomSheetAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutPlaylistBinding.inflate(inflater, container, false)
        _bottomSheetBinding = PlaylistBottomSheetBinding.bind(binding.playlistBottomSheet.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetAdapter = TrackAdapter(
            onTrackClickListener = { track ->
                openAudioPlayer(track)
            },
            onTrackLongClickListener = { track ->
                showConfirmDialog(track)
            }
        )
        bottomSheetBinding.rvTrack.adapter = bottomSheetAdapter
        bottomSheetBinding.rvTrack.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.btnBack.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.playlist.observe(viewLifecycleOwner){
            renderPlaylist(it)
        }
        viewModel.tracks.observe(viewLifecycleOwner){
            renderTracks(it)
        }
    }

    private fun renderTracks(tracks: List<TrackModel>) {
        bottomSheetAdapter.apply {
            trackList.clear()
            trackList.addAll(tracks)
            notifyDataSetChanged()
        }
    }

    private fun openAudioPlayer(track: TrackModel){
        if (clickDebounce()){
            findNavController().navigate(
                R.id.action_playlistDetailFragment_to_audioPlayerFragment2,
                AudioPlayerFragment.createARgs(track)
            )
        }
    }
    private fun showConfirmDialog(track: TrackModel){
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialAlertDialog)
            .setTitle(getString(R.string.playlist_detail_confirm_dialog_title))
            .setPositiveButton(getString(R.string.playlist_detail_confirm_dialog_positive_button)) { dialog, which ->
                viewModel.removeTrack(track.trackId)
            }.setNegativeButton(getString(R.string.playlist_detail_confirm_dialog_negative_button), null)
            .show()
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

    private fun renderPlaylist(uiModel: PlaylistUiModel) {
        binding.apply {
            tvTitle.text = uiModel.title
            if(uiModel.description == null){
                tvDescription.visibility = View.GONE
            } else{
                tvDescription.text = uiModel.description
            }
            tvPropertyTotalDuration.text = resources.getQuantityString(
                R.plurals.minutes,
                uiModel.totalDuration,
                uiModel.totalDuration
            )
            tvPropertyTotalTracks.text = resources.getQuantityString(
                R.plurals.tracks_count,
                uiModel.totalTracks,
                uiModel.totalTracks
            )
        }
        showPoster(uiModel.uri)
    }

    private fun showPoster(uri: String?){
        Glide.with(this)
            .load(uri)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(cornerRadius)
                )
            )
            .placeholder(R.drawable.ic_placeholder_312)
            .into(binding.ivPosterPlaylist)
    }

    override fun onResume() {
        super.onResume()
        hideBottomNav()
    }

    override fun onPause() {
        super.onPause()
        showBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomSheetBinding = null
    }

    private fun hideBottomNav(){
        (requireActivity() as? BottomNavigationOwner)?.hideBottomNav()
    }
    private fun showBottomNav(){
        (requireActivity() as? BottomNavigationOwner)?.showBottomNav()
    }

    companion object{
        private const val CLICK_DELAY = 1000L

        private const val ARGS_PLAYLIST = "playlist"

        fun createARgs(playlistId: Int) = Bundle().apply {
            putInt(ARGS_PLAYLIST, playlistId)
        }
    }

}