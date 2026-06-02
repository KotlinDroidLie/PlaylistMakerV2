package com.practicum.playlistmaker.features.search.domain.impl

import com.practicum.playlistmaker.features.search.data.dto.Resource
import com.practicum.playlistmaker.features.search.domain.api.repo.IRemoteTrackRepository
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksUseCase(
    private val repository: IRemoteTrackRepository,
) : ISearchTracksUseCase {

    override fun searchTracks(expression: String): Flow<SearchResult> {
        return repository.doRequest(expression).map { result ->
            when(result){
                is Resource.Error -> {
                    SearchResult.Error(result.message, result.extraMessage)
                }

                is Resource.Success -> {
                    SearchResult.Success(result.data)
                }

            }
        }
    }

}