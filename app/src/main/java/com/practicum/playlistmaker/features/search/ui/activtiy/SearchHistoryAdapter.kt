package com.practicum.playlistmaker.features.search.ui.activtiy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.databinding.SongViewBinding

class SearchHistoryAdapter(private val onTrackClickListener: OnTrackClickListener): RecyclerView.Adapter<SearchViewHolder>() {
    var tracks: MutableList<TrackModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SongViewBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onTrackClickListener.openAudioPlayer(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}