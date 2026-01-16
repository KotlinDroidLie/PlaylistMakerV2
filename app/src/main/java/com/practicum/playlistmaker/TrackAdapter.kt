package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val trackList: MutableList<TrackModel>,private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<SearchViewHolder>() {
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
        holder.itemView.setOnClickListener { onItemClickListener.addToSearchHistory(trackList[position]) }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}