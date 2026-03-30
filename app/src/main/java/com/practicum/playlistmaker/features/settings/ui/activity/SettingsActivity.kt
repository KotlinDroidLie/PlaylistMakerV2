package com.practicum.playlistmaker.features.settings.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.core.di.Creator
import com.practicum.playlistmaker.features.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linear_layout_settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory(
            Creator.getSettingsUseCase(applicationContext),
            Creator.getSharingUseCase(this)
        )).get(SettingsViewModel::class.java)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.sw_theme)
        val buttonBack = findViewById<MaterialToolbar>(R.id.btn_settings_back)
        val buttonShareApp = findViewById<Button>(R.id.btn_share_app)
        val buttonWriteSupport = findViewById<Button>(R.id.btn_write_support)
        val buttonUserAgreement = findViewById<Button>(R.id.btn_user_agreement)

        viewModel.themeSwitcher.observe(this){
            themeSwitcher.isChecked = it
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        buttonShareApp.setOnClickListener {
            viewModel.shareApp()
        }

        buttonWriteSupport.setOnClickListener {
            viewModel.openSupport()
        }

        buttonUserAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }
}