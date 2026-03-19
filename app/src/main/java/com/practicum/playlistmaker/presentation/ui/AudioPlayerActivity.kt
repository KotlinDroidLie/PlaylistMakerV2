package com.practicum.playlistmaker.presentation.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.di.Creator
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackDurationUseCase
import com.practicum.playlistmaker.domain.api.usecase.FormatTrackYearUseCase
import com.practicum.playlistmaker.domain.api.usecase.LoadImageUseCase
import com.practicum.playlistmaker.domain.models.TrackModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private val mediaPlayer = MediaPlayer()
    private lateinit var playbackPosition: TextView
    private val mainHandler = Handler(Looper.getMainLooper())
    private var mediaPlayerState = STATE_DEFAULT
    private lateinit var loadImageUseCase: LoadImageUseCase
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

        loadImageUseCase = Creator.getLoadImageUseCase(this)
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

        val audioUrl = track.audioPreviewUrl
        preparedMediaPlayer(audioUrl)

        loadImageUseCase.execute(track.getCoverArtwork(),posterSong)
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
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        pauseProgressUpdate()
    }
    private fun preparedMediaPlayer(audioUrl: String){
        with(mediaPlayer){
            setDataSource(audioUrl)
            prepareAsync()
            setOnPreparedListener {
                mediaPlayerState = STATE_PREPARED
            }
            setOnCompletionListener {
                buttonPlay.setImageResource(R.drawable.ic_button_play_music_100)
                mediaPlayerState = STATE_PREPARED
                pauseProgressUpdate()
                playbackPosition.text = "00:00"
            }
        }
    }

    private fun startPlayer(){
        mediaPlayer.start()
        buttonPlay.setImageResource(R.drawable.ic_button_pause_music_100)
        mediaPlayerState = STATE_PLAYING
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
        buttonPlay.setImageResource(R.drawable.ic_button_play_music_100)
        mediaPlayerState = STATE_PAUSED
    }
    private fun controlMediaPlayer(){
        when(mediaPlayerState) {
            STATE_PLAYING -> {
                pausePlayer()
                pauseProgressUpdate()
            }
            STATE_PAUSED, STATE_PREPARED ->{
                startPlayer()
                startProgressUpdate()
            }
        }
    }

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            playbackPosition.text = currentPosition
            mainHandler.postDelayed(this, 500)
        }

    }

    private fun startProgressUpdate(){
        updateProgressRunnable.run()
    }
    private fun pauseProgressUpdate(){
        mainHandler.removeCallbacks(updateProgressRunnable)
    }
    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        const val KEY_TRACK = "track"
    }
}