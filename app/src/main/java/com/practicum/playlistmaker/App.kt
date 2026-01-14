package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App : Application() {
    private lateinit var settingPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        settingPrefs = getSharedPreferences(SETTING_PREFERENCE, MODE_PRIVATE)
        val darkThemeEnable = isDarkThemeEnable()
        switchTheme(darkThemeEnable)
    }

    fun switchTheme(darkThemeEnable: Boolean) {
        settingPrefs.edit {
            putBoolean(KEY_SWITCH_THEME, darkThemeEnable)
                .apply()
        }

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnable) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun isDarkThemeEnable() = settingPrefs.getBoolean(KEY_SWITCH_THEME, false)
    companion object{
        const val SETTING_PREFERENCE = "setting_preference"
        const val KEY_SWITCH_THEME = "key_switch_theme"
    }
}