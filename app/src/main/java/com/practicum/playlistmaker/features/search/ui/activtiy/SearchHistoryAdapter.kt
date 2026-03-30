package com.practicum.playlistmaker.features.search.ui.activtiy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.models.TrackModel

class SearchHistoryAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<SearchViewHolder>() {
    var trackHistory: MutableList<TrackModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_view, parent, false)
        return SearchViewHolder(view)
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