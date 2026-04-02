package com.practicum.playlistmaker.di.data

import com.practicum.playlistmaker.features.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.features.sharing.domain.api.IExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val externalNavigatorModule = module {
    single<IExternalNavigator>{
        ExternalNavigator(androidContext())
    }
}