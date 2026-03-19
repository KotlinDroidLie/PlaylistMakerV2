package com.practicum.playlistmaker.presentation.ui

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.di.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.usecase.ShareAppUseCase
import com.practicum.playlistmaker.domain.api.usecase.SwitchThemeUseCase
import com.practicum.playlistmaker.domain.api.usecase.WriteSupportUseCase
import com.practicum.playlistmaker.domain.api.usecase.UserAgreementUseCase
import com.practicum.playlistmaker.domain.impl.ShareAppUseCaseImpl
import com.practicum.playlistmaker.domain.impl.WriteSupportUseCaseImpl
import com.practicum.playlistmaker.domain.impl.UserAgreementUseCaseImpl

class SettingsActivity : AppCompatActivity() {
    private lateinit var shareAppUseCase: ShareAppUseCase
    private lateinit var writeSupportUseCase: WriteSupportUseCase
    private lateinit var userAgreementUseCase: UserAgreementUseCase
    private lateinit var switchThemeUseCase: SwitchThemeUseCase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linear_layout_settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        shareAppUseCase = Creator.getShareAppUseCase(this)
        writeSupportUseCase = Creator.getWriteSupportUseCase(this)
        userAgreementUseCase = Creator.getUserAgreementUseCase(this)
        switchThemeUseCase = Creator.getSwitchThemeUseCase(this)

        
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.sw_theme)
        val buttonBack = findViewById<MaterialToolbar>(R.id.btn_settings_back)
        val buttonShareApp = findViewById<Button>(R.id.btn_share_app)
        val buttonWriteSupport = findViewById<Button>(R.id.btn_write_support)
        val buttonUserAgreement = findViewById<Button>(R.id.btn_user_agreement)

        themeSwitcher.isChecked = switchThemeUseCase.isDarkThemeEnable()
        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            switchThemeUseCase.switchTheme(isChecked)
        }

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        buttonShareApp.setOnClickListener {
            val shareIntent = shareAppUseCase.execute()
            startActivity(shareIntent)
        }

        buttonWriteSupport.setOnClickListener {
            val supportIntent = writeSupportUseCase.execute()
            startActivity(supportIntent)
        }

        buttonUserAgreement.setOnClickListener {
            val userAgreementIntent = userAgreementUseCase.execute()
            startActivity(userAgreementIntent)
        }
    }
}