package com.practicum.playlistmaker.features.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.features.search.ui.OnItemClickListener
import com.practicum.playlistmaker.features.search.ui.SearchViewHolder

class TrackAdapter(private val trackList: MutableList<TrackModel>, private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<SearchViewHolder>() {
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