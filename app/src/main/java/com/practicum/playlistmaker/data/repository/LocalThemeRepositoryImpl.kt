package com.practicum.playlistmaker.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.practicum.playlistmaker.domain.api.repo.ThemeRepository2

class LocalThemeRepositoryImpl(context: Context): ThemeRepository2 {

    private val  settingPrefs = context.getSharedPreferences(SETTING_PREFERENCE, MODE_PRIVATE)

    override fun switchTheme(isChecked: Boolean) {
        settingPrefs.edit {
            putBoolean(KEY_SWITCH_THEME,isChecked)
        }

        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun isDarkThemeEnable() = settingPrefs.getBoolean(KEY_SWITCH_THEME, false)


    private companion object{
        private const val SETTING_PREFERENCE = "setting_preference"
        private const val KEY_SWITCH_THEME = "key_switch_theme"
    }
}