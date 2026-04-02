package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.features.player.domain.api.IFormatTrackUseCase
import com.practicum.playlistmaker.features.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module{
    includes(playerUseCaseModule)
    viewModel{ (model: TrackModel) ->
        PlayerViewModel(model, get<IFormatTrackUseCase>())
    }
}