package com.practicum.playlistmaker.features.media.ui.activtiy

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistViewGridBinding
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

class PlaylistViewHolder(
    private val binding: PlaylistViewGridBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        8f,
        binding.root.context.resources.displayMetrics
    ).toInt()

    fun bind(playlist: PlaylistModel){
        binding.tvPlaylistTitle.text = playlist.title
        binding.tvPlaylistTotalTracks.text = binding.root.context.resources.getQuantityString(
            R.plurals.tracks_count,
            playlist.totalTracks,
            playlist.totalTracks
        )
        Glide.with(itemView.context)
            .load(playlist.uri)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(cornerRadius)
                )
            )
            .placeholder(R.drawable.ic_placeholder_312)
            .into(binding.ivPosterPlaylist)

    }
}