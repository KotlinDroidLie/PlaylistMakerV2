package com.practicum.playlistmaker.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linear_layout_settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.sw_theme)

//        themeSwitcher.isChecked = (applicationContext as App).isDarkThemeEnable()

        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
//            (applicationContext as App).switchTheme(isChecked)
        }

        val buttonBack = findViewById<MaterialToolbar>(R.id.btn_settings_back)
        buttonBack.setNavigationOnClickListener {
            finish()
        }

        val buttonShareApp = findViewById<Button>(R.id.btn_share_app)
        buttonShareApp.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,getString(R.string.link_share_app))
            }
            startActivity(shareIntent)
        }

        val buttonWriteSupport = findViewById<Button>(R.id.btn_write_support)
        buttonWriteSupport.setOnClickListener {
            val supportIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL,arrayOf(getString(R.string.my_mail)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail_title))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_mail_text))
            }
            startActivity(supportIntent)
        }

        val buttonUserAgreement = findViewById<Button>(R.id.btn_user_agreement)
        buttonUserAgreement.setOnClickListener {
            val userAgreementIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = getString(R.string.link_user_agreement).toUri()
            }
            startActivity(userAgreementIntent)
        }
    }
}