package com.practicum.playlistmaker.features.search.domain.api.usecase

import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.core.data.dto.Resource

interface IGetSearchHistoryUseCase {
    fun execute(): Resource<List<TrackModel>>
}