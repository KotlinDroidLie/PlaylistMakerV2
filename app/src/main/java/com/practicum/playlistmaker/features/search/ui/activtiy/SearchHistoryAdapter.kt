package com.practicum.playlistmaker.features.search.ui.activtiy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.databinding.SongViewBinding

class SearchHistoryAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<SearchViewHolder>() {
    var trackHistory: MutableList<TrackModel> = mutableListOf()
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
        holder.bind(trackHistory[position])
        holder.itemView.setOnClickListener { onItemClickListener.openAudioPlayer(trackHistory[position]) }
    }

    override fun getItemCount(): Int {
        return trackHistory.size
    }
}