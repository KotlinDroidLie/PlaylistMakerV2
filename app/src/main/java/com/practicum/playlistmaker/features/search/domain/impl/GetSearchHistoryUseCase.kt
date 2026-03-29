package com.practicum.playlistmaker.features.search.domain.impl

import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.core.data.dto.Resource
import com.practicum.playlistmaker.features.search.domain.api.repo.ISearchHistoryRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.IGetSearchHistoryUseCase

class GetSearchHistoryUseCase(
    private val historyRepository: ISearchHistoryRepository
) : IGetSearchHistoryUseCase {
    override fun execute(): Resource<List<TrackModel>> = historyRepository.getHistory()
}