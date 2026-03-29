package com.practicum.playlistmaker.presentation.ui

import android.media.MediaPlayer
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.di.Creator
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackDurationUseCase
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackYearUseCase
import com.practicum.playlistmaker.domain.models.TrackModel
import com.practicum.playlistmaker.presentation.MediaController
import com.practicum.playlistmaker.presentation.PlaybackState

class AudioPlayerActivity : AppCompatActivity() {
    private val mediaPlayer = MediaPlayer()
    private lateinit var mediaController: MediaController
    private lateinit var playbackPosition: TextView
    private lateinit var formatTrackYearUseCase: FormatTrackYearUseCase
    private lateinit var formatTrackDurationUseCase: FormatTrackDurationUseCase
    private lateinit var buttonPlay: ImageButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_audio_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        formatTrackYearUseCase = Creator.getFormatTrackYearUseCase()
        formatTrackDurationUseCase = Creator.getFormatTrackDurationUseCase()
        playbackPosition = findViewById(R.id.tv_current_time_song)

        val buttonBack = findViewById<MaterialToolbar>(R.id.btn_audio_player_back)
        buttonPlay = findViewById(R.id.ibtn_music)

        buttonPlay.setOnClickListener {
            controlMediaPlayer()
        }

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        val posterSong = findViewById<ImageView>(R.id.iv_poster_song_player)
        val songName = findViewById<TextView>(R.id.tv_name_song_player)
        val groupName = findViewById<TextView>(R.id.tv_group_name_player)
        val durationSong = findViewById<TextView>(R.id.tv_duration_song_player)
        val albumName = findViewById<TextView>(R.id.tv_album_song_player)
        val yearSong = findViewById<TextView>(R.id.tv_year_song_player)
        val genreSong = findViewById<TextView>(R.id.tv_genre_song_player)
        val country = findViewById<TextView>(R.id.tv_country_song_player)
        val albumDescriptionGroup = findViewById<Group>(R.id.group_album_name)
        val yearTrackGroup = findViewById<Group>(R.id.group_year_song)

        val track: TrackModel? = intent.getParcelableExtra(KEY_TRACK)
        track ?: run {
            Toast.makeText(this, resources.getString(R.string.error_failed_load_track), Toast.LENGTH_SHORT).show()
            finish()
            return
            }

        mediaController = MediaController(mediaPlayer)

        mediaController.setPlaybackPositionCallback { position ->
            playbackPosition.text = position
        }
        
        mediaController.setPlaybackCompletionCallback {
            buttonPlay.setImageResource(R.drawable.ic_button_play_music_100)
        }

        val audioUrl = track.audioPreviewUrl
        mediaController.prepareMedia(audioUrl)

        val cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            this.resources.displayMetrics
        ).toInt()
        

        Glide.with(this)
            .load(track.getCoverArtwork())
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_312)
            .into(posterSong)
        songName.text = track.trackName
        groupName.text = track.artistName
        durationSong.text = formatTrackDurationUseCase.execute(track.trackDuration)
        genreSong.text = track.genre
        country.text = track.country

        if(track.albumName != null){
            albumName.text = track.albumName
        } else {
            albumDescriptionGroup.isVisible = false
        }

        val date = formatTrackYearUseCase.execute(track.releaseDate)
        if(date.isNotEmpty()){
            yearSong.text = date
        } else {
            yearTrackGroup.isVisible = false
        }

    }

    override fun onPause() {
        super.onPause()
        mediaController.pause()
        updatePlayButtonIcon()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaController.release()
    }
    
    private fun controlMediaPlayer(){
        when(mediaController.getCurrentState()) {
            PlaybackState.PLAYING -> mediaController.pause()
            PlaybackState.PAUSED, PlaybackState.PREPARED -> mediaController.play()
            PlaybackState.DEFAULT -> {}
        }
        updatePlayButtonIcon()
    }
    private fun updatePlayButtonIcon() {
        val icon = when (mediaController.getCurrentState()) {
            PlaybackState.PLAYING -> R.drawable.ic_button_pause_music_100
            else -> R.drawable.ic_button_play_music_100
        }
        buttonPlay.setImageResource(icon)
    }
    companion object{
        const val KEY_TRACK = "track"
    }
}