package com.practicum.playlistmaker.features.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.features.search.ui.activtiy.SearchFragment
import com.practicum.playlistmaker.features.settings.ui.activity.SettingsFragment


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSearch.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fcv_main, SearchFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }

        binding.btnMedia.setOnClickListener {
//            val intentMedia = Intent(this@MainActivity, MediaActivity::class.java)
//            startActivity(intentMedia)
            TODO()

        }

        binding.btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fcv_main, SettingsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}