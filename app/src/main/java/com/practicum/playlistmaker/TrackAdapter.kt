package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val trackList: List<TrackModel>): RecyclerView.Adapter<SearchViewHolder>() {
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
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}