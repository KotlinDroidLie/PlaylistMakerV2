package com.practicum.playlistmaker.presentation

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.TrackModel

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
        duration.text = model.formatTrackDuration()
        artist.text = model.artistName
        Glide.with(itemView.context)
            .load(model.trackImage)
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.ic_placeholder_45)
            .into(poster)
    }
}