package com.practicum.playlistmaker.features.player.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerUiModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerViewModel

class AudioPlayerActivity : AppCompatActivity() {
    private val cornerRadius by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            this.resources.displayMetrics
        ).toInt()
    }
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_audio_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val track: TrackModel? = intent.getParcelableExtra(KEY_TRACK)
        track ?: run {
            Toast.makeText(this, resources.getString(R.string.error_failed_load_track), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track, Creator.getFormatTrackUseCase()))
            .get(PlayerViewModel::class.java)

        binding.ibtnMusic.setOnClickListener {
            viewModel.playerControl()
        }
        binding.btnAudioPlayerBack.setNavigationOnClickListener {
            finish()
        }
        viewModel.track.observe(this){
            initView(it)
        }
        viewModel.playerState.observe(this){
            updatePlayButtonIcon(it == PlayerViewModel.STATE_PLAYING)
            enableButton( it != PlayerViewModel.STATE_DEFAULT)
        }
        viewModel.timer.observe(this){
            updateTimer(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
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
    companion object{
        const val KEY_TRACK = "track"
    }
}