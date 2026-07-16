package com.practicum.playlistmaker.features.media.ui.activtiy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistViewGridBinding
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

class PlaylistGridAdapter(
    private val onPlaylistClickListener: OnPlaylistClickListener
): RecyclerView.Adapter<PlaylistGridViewHolder>() {
    var playlists: MutableList<PlaylistModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistViewGridBinding.inflate(inflater, parent, false )
        return PlaylistGridViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistGridViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlaylistClickListener.navigate(playlists[position].id)
        }
    }

    override fun getItemCount() = playlists.size
}