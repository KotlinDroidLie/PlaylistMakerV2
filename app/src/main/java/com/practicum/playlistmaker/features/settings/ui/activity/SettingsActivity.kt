package com.practicum.playlistmaker.features.settings.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.features.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linear_layout_settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory(
            Creator.getSettingsUseCase(applicationContext),
            Creator.getSharingUseCase(applicationContext)
        )).get(SettingsViewModel::class.java)

        viewModel.themeSwitcher.observe(this){
            binding.swTheme.isChecked = it
        }

        binding.swTheme.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.btnSettingsBack.setNavigationOnClickListener {
            finish()
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
}