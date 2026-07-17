package com.practicum.playlistmaker.features.media.ui.activtiy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.features.media.ui.activtiy.favourite.FavouriteTracksFragment
import com.practicum.playlistmaker.features.media.ui.activtiy.playlists.PlaylistFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager, lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FavouriteTracksFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }

    override fun getItemCount() = 2
}