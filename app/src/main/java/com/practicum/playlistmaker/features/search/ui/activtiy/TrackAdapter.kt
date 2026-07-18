package com.practicum.playlistmaker.features.search.ui.activtiy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.databinding.SongViewBinding

class TrackAdapter(
    private val onTrackClickListener: (TrackModel) -> Unit,
    private val onTrackLongClickListener: ((TrackModel) -> Unit)? = null,
): RecyclerView.Adapter<TrackViewHolder>() {
    var trackList: MutableList<TrackModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SongViewBinding.inflate(inflater, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onTrackClickListener(trackList[position])
        }
        onTrackLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                it(trackList[position])
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}