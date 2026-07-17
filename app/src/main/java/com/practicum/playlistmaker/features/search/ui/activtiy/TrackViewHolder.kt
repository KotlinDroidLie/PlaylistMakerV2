package com.practicum.playlistmaker.features.search.ui.activtiy

import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.databinding.SongViewBinding
import java.util.Locale

class TrackViewHolder(private val binding: SongViewBinding): RecyclerView.ViewHolder(binding.root) {
    private val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        2f,
        binding.root.context.resources.displayMetrics
    ).toInt()

    fun bind(model: TrackModel){
        binding.tvNameSong.text = model.trackName
        binding.tvDurationSong.text = formatTrackDuration(model.trackDuration)
        binding.tvGroupName.text = model.artistName
        Glide.with(itemView.context)
            .load(model.trackImage)
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_45)
            .into(binding.ivPosterSong)
    }
    private fun formatTrackDuration(trackDuration: Int): String = SimpleDateFormat(
        "mm:ss",
        Locale.getDefault()
    ).format(trackDuration)
}