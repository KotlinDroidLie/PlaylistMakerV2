package com.practicum.playlistmaker.features.player.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.databinding.PlaylistBottomSheetBinding
import com.practicum.playlistmaker.features.main.BottomNavigationOwner
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlaylistBottomSheetViewModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerState
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerUiModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlaylistBottomSheetState
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {

    private var _bottomSheetBinding: PlaylistBottomSheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    private lateinit var playlistAdapter: PlaylistAdapter
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PlayerViewModel by viewModel{
        parametersOf(requireArguments().getParcelable<TrackModel>(ARGS_TRACK))
    }

    private val bottomSheetViewModel: PlaylistBottomSheetViewModel by viewModel()

    private val cornerRadius by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            this.resources.displayMetrics
        ).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        _bottomSheetBinding = PlaylistBottomSheetBinding.bind(binding.bottomSheet.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter()
        bottomSheetBinding.rvPlaylist.adapter = playlistAdapter
        bottomSheetBinding.rvPlaylist.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBinding.bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val alpha = (slideOffset + 1f) / 2f
                val colorArgb = Color.argb(alpha,0F,0F,0F)
                binding.viewOverlay.setBackgroundColor(colorArgb)
            }

        })

        bottomSheetBinding.btnAddNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_audioPlayerFragment_to_createPlaylistFragment
            )
        }

        binding.ibtnAddToFavorite.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
        binding.ibtnAddToPlaylist.setOnClickListener {
            bottomSheetViewModel.openBottomSheet()
        }

        binding.ibtnMusic.setOnClickListener {
            viewModel.playerControl()
        }
        binding.btnAudioPlayerBack.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.playbackState.observe(viewLifecycleOwner){
            renderPlaybackControl(it)
        }

        viewModel.trackState.observe(viewLifecycleOwner){
            renderUI(it)
        }

        bottomSheetViewModel.state.observe(viewLifecycleOwner){
            renderBottomSheetUi(it)
        }

    }

    override fun onResume() {
        super.onResume()
        hideBottomNav()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        showBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomSheetBinding = null
    }
    private fun renderBottomSheetUi(state: PlaylistBottomSheetState) {
        when(state){
            PlaylistBottomSheetState.Empty -> {
                showBottomSheetEmpty()
            }
            is PlaylistBottomSheetState.Content ->{
                showBottomSheetContent(state.playlists)
            }
            PlaylistBottomSheetState.Hide ->{
                hideBottomSheet()
            }
        }
    }

    private fun showBottomSheetEmpty() {
        showBottomSheet()
    }

    private fun showBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showBottomSheetContent(playlists: List<PlaylistModel>) {
        showBottomSheet()
        updateBottomSheetContent(playlists)
    }

    private fun updateBottomSheetContent(playlists: List<PlaylistModel>) {
        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()
    }

    private fun updateTimer(time: String){
        binding.tvCurrentTimeSong.text = time
    }
    private fun enableButton(flag:Boolean){
        binding.ibtnMusic.isEnabled = flag
    }
    private fun updatePlayButtonIcon(flag: Boolean) {
        val icon = when (flag) {
            false -> R.drawable.ic_button_pause_music_100
            true -> R.drawable.ic_button_play_music_100
        }
        binding.ibtnMusic.setImageResource(icon)
    }
    private fun initView(model: PlayerUiModel){
        binding.apply {
            tvNameSongPlayer.text = model.trackName
            tvGroupNamePlayer.text = model.artistName
            tvDurationSongPlayer.text = model.trackDuration
            tvGenreSongPlayer.text = model.genre
            tvCountrySongPlayer.text = model.country
        }
        model.albumName?.let {
            binding.tvAlbumSongPlayer.text = it
        } ?: run {
            binding.groupAlbumName.isVisible = false
        }
        model.releaseDate?.let {
            binding.tvYearSongPlayer.text = it
        } ?: run {
            binding.groupYearSong.isVisible = false
        }
        showPoster(model.trackImage)
    }
    private fun showPoster(url: String){
        Glide.with(this)
            .load(url)
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(binding.ivPosterSongPlayer)
    }
    private fun renderPlaybackControl(state: PlayerState){
        enableButton(state.buttonIsEnable)
        updatePlayButtonIcon(state.buttonIsPlayIcon)
        updateTimer(state.progress)
    }

    private fun renderUI(trackUiModel: PlayerUiModel){
        initView(trackUiModel)
        updateFavouriteButton(trackUiModel.isFavourite)
    }

    private fun updateFavouriteButton(isFavourite: Boolean) {
        val icon = when(isFavourite){
            true -> R.drawable.ic_favourite_button_enable_51
            false -> R.drawable.ic_button_add_to_favorite_51
        }
        binding.ibtnAddToFavorite.setImageResource(icon)
    }
    private fun hideBottomNav(){
        (requireActivity() as? BottomNavigationOwner)?.hideBottomNav()
    }
    private fun showBottomNav(){
        (requireActivity() as? BottomNavigationOwner)?.showBottomNav()
    }

    companion object{
        private const val ARGS_TRACK = "track"

        fun createARgs(model: TrackModel) = Bundle().apply {
                putParcelable(ARGS_TRACK, model)
        }
    }
}