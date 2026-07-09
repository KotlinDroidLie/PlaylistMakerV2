package com.practicum.playlistmaker.features.media.ui.activtiy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistViewGridBinding
import com.practicum.playlistmaker.features.media.domain.model.PlaylistModel

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {
    var playlists: MutableList<PlaylistModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlaylistViewGridBinding.inflate(inflater, parent, false )
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
    }

    override fun getItemCount() = playlists.size
}