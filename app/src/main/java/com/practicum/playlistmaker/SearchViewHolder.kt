package com.practicum.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textview.MaterialTextView

class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val name = itemView.findViewById<MaterialTextView>(R.id.tv_name_song)
    private val poster = itemView.findViewById<ImageView>(R.id.iv_poster_song)
    private val duration = itemView.findViewById<MaterialTextView>(R.id.tv_duration_song)
    private val artist = itemView.findViewById<MaterialTextView>(R.id.tv_group_name)

    fun bind(model: TrackModel){
        name.text = model.trackName
        Glide.with(itemView.context).load(model.artworkUrl100).transform(RoundedCorners(2)).placeholder(R.drawable.ic_placeholder_45).into(poster)
        duration.text = model.trackTime
        artist.text = model.artistName
    }
}