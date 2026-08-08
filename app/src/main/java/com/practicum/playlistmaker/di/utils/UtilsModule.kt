package com.practicum.playlistmaker.di.utils

import com.practicum.playlistmaker.utils.NetworkStateReceiver
import org.koin.dsl.module

val utilsModule = module {
    single{
        NetworkStateReceiver()
    }
}