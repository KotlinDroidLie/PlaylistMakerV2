package com.practicum.playlistmaker.features.search.ui.activtiy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.core.models.TrackModel
import com.practicum.playlistmaker.databinding.SongViewBinding

class SearchTrackAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<SearchViewHolder>() {
    var trackList: MutableList<TrackModel> = mutableListOf()
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
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.addToSearchHistory(trackList[position])
            onItemClickListener.openAudioPlayer(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}