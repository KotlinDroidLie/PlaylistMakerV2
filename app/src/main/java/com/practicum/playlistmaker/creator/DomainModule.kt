package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import com.practicum.playlistmaker.features.player.domain.impl.FormatTrackUseCase
import org.koin.dsl.module

val playerUseCaseModule = module{
     single<IFormatTrackUseCase>{
         FormatTrackUseCase()
     }
 }