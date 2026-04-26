package com.practicum.playlistmaker.features.settings.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.features.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeSwitcher.observe(viewLifecycleOwner){
            binding.swTheme.isChecked = it
        }

        binding.swTheme.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.btnShareApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.btnWriteSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.btnUserAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}