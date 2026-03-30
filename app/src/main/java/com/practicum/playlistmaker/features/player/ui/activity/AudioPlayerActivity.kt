package com.practicum.playlistmaker.features.player.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.core.di.Creator
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerUiModel
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerViewModel

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel
    private lateinit var playbackPosition: TextView
    private lateinit var buttonPlay: ImageButton
    private lateinit var posterSong: ImageView
    private lateinit var songName: TextView
    private lateinit var groupName: TextView
    private lateinit var durationSong: TextView
    private lateinit var albumName: TextView
    private lateinit var yearSong: TextView
    private lateinit var genreSong: TextView
    private lateinit var country: TextView
    private lateinit var albumDescriptionGroup: Group
    private lateinit var yearTrackGroup: Group
    private lateinit var buttonBack: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
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

        playbackPosition = findViewById(R.id.tv_current_time_song)
        buttonBack = findViewById<MaterialToolbar>(R.id.btn_audio_player_back)
        buttonPlay = findViewById(R.id.ibtn_music)
        posterSong = findViewById<ImageView>(R.id.iv_poster_song_player)
        songName = findViewById<TextView>(R.id.tv_name_song_player)
        groupName = findViewById<TextView>(R.id.tv_group_name_player)
        durationSong = findViewById<TextView>(R.id.tv_duration_song_player)
        albumName = findViewById<TextView>(R.id.tv_album_song_player)
        yearSong = findViewById<TextView>(R.id.tv_year_song_player)
        genreSong = findViewById<TextView>(R.id.tv_genre_song_player)
        country = findViewById<TextView>(R.id.tv_country_song_player)
        albumDescriptionGroup = findViewById<Group>(R.id.group_album_name)
        yearTrackGroup = findViewById<Group>(R.id.group_year_song)

        buttonPlay.setOnClickListener {
            viewModel.playerControl()
        }
        buttonBack.setNavigationOnClickListener {
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
        playbackPosition.text = time
    }
    private fun enableButton(flag:Boolean){
        buttonPlay.isEnabled = flag
    }
    private fun updatePlayButtonIcon(flag: Boolean) {
        val icon = when (flag) {
            true -> R.drawable.ic_button_pause_music_100
            false -> R.drawable.ic_button_play_music_100
        }
        buttonPlay.setImageResource(icon)
    }
    private fun initView(model: PlayerUiModel){
        songName.text = model.trackName
        groupName.text = model.artistName
        durationSong.text = model.trackDuration
        genreSong.text = model.genre
        country.text = model.country
        model.albumName?.let {
            albumName.text = it
        } ?: run {
            albumDescriptionGroup.isVisible = false
        }
        model.releaseDate?.let {
            yearSong.text = it
        } ?: run {
            yearTrackGroup.isVisible = false
        }
        showPoster(model.trackImage)
    }
    private fun showPoster(url: String){
        Glide.with(this)
            .load(url)
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(posterSong)
    }
    private val cornerRadius by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            this.resources.displayMetrics
        ).toInt()
    }
    companion object{
        const val KEY_TRACK = "track"
    }
}