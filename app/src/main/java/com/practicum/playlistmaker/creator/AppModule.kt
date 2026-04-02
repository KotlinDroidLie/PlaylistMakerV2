package com.practicum.playlistmaker.creator

import org.koin.dsl.module

val appModule = module{
    includes(playerViewModelModule)
}