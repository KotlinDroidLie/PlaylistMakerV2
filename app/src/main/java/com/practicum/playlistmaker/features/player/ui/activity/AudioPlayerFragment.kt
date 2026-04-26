package com.practicum.playlistmaker.features.player.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerState
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerUiModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel{
        parametersOf(requireArguments().getParcelable<TrackModel>(ARGS_TRACK))
    }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibtnMusic.setOnClickListener {
            viewModel.playerControl()
        }
        binding.btnAudioPlayerBack.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.state.observe(viewLifecycleOwner){
            render(it)
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateTimer(time: String){
        binding.tvCurrentTimeSong.text = time
    }
    private fun enableButton(flag:Boolean){
        binding.ibtnMusic.isEnabled = flag
    }
    private fun updatePlayButtonIcon(flag: Boolean) {
        val icon = when (flag) {
            true -> R.drawable.ic_button_pause_music_100
            false -> R.drawable.ic_button_play_music_100
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
    private fun render(state: PlayerState){
        initView(state.track)
        enableButton(state.playerStatus != PlayerViewModel.STATE_DEFAULT)
        updatePlayButtonIcon(state.playerStatus == PlayerViewModel.STATE_PLAYING)
        updateTimer(state.timer)
    }
    companion object{
        private const val ARGS_TRACK = "track"

        fun createARgs(model: TrackModel) = Bundle().apply {
                putParcelable(ARGS_TRACK, model)
        }
    }
}