package com.practicum.playlistmaker.features.search.ui

import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.TrackModel
import java.util.Locale

class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<MaterialTextView>(R.id.tv_name_song)
    private val poster = itemView.findViewById<ImageView>(R.id.iv_poster_song)
    private val duration = itemView.findViewById<MaterialTextView>(R.id.tv_duration_song)
    private val artist = itemView.findViewById<MaterialTextView>(R.id.tv_group_name)
    private val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        2f,
        itemView.context.resources.displayMetrics
    ).toInt()

    fun bind(model: TrackModel){
        name.text = model.trackName
        duration.text = formatTrackDuration(model.trackDuration)
        artist.text = model.artistName
        Glide.with(itemView.context)
            .load(model.trackImage)
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_45)
            .into(poster)
    }
    private fun formatTrackDuration(trackDuration: Int): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDuration)
}