package com.practicum.playlistmaker.features.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.features.settings.domain.api.ISettingsUseCase
import com.practicum.playlistmaker.features.sharing.domain.api.ISharingUseCase
class SettingsViewModel(
    private val settingsUseCase: ISettingsUseCase,
    private val sharingUseCase: ISharingUseCase
    ): ViewModel() {
    companion object{
        fun getViewModelFactory(
            settingsUseCase: ISettingsUseCase,
            sharingUseCase: ISharingUseCase
        ) = viewModelFactory {
            initializer {
                SettingsViewModel(settingsUseCase, sharingUseCase)
            }
        }
    }
    private val _themeSwitcher = MutableLiveData<Boolean>(settingsUseCase.getSettings().isDarkThemeEnable)
    val themeSwitcher: LiveData<Boolean> = _themeSwitcher
    fun switchTheme(isChecked : Boolean){
        _themeSwitcher.postValue(isChecked)
        settingsUseCase.switchTheme(isChecked)
    }
    fun shareApp(){
        sharingUseCase.shareApp()
    }
    fun openSupport(){
        sharingUseCase.openSupport()
    }
    fun openTerms(){
        sharingUseCase.openTerms()
    }
}