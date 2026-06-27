package com.practicum.playlistmaker.features.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteUseCase
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.domain.api.usecase.IHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.SearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val historyUseCase: IHistoryUseCase,
    private val searchTracksUseCase: ISearchTracksUseCase,
    private val favouriteTracksUseCase: IFavouriteUseCase
) : ViewModel() {

    private val _historyTracks = MutableLiveData<List<TrackModel>>()
    val historyTracks: LiveData<List<TrackModel>> = _historyTracks
    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    init {
        refreshHistory()
        updateStatusContent()
    }
    private var lastSearchText: String = ""
    private var lastErrorSearch: String? = null
    private var searchJob: Job? = null
    fun saveToHistory(track: TrackModel) {
        historyUseCase.saveToHistory(track)
    }

    fun clearHistory() {
        historyUseCase.clearHistory()
        refreshHistory()
    }

    fun resetToDefault(){
        renderState(SearchState.Default)
    }

    fun loadHistory(){
        renderState(SearchState.HistoryResult)
    }

    private fun updateStatusContent() {
        viewModelScope.launch {
            favouriteTracksUseCase.getTracks().collect { tracks ->
                val currentState = _state.value
                if (currentState is SearchState.SearchResult) {
                    updateSearchResult(tracks, currentState)
                }
            }
        }
    }

    private fun updateSearchResult(tracks: List<TrackModel>, currentState: SearchState.SearchResult) {
        val tracksFavouriteIds = tracks.map { track ->
            track.trackId
        }
        val updatedTracks = currentState.tracks.map { track ->
            track.copy(isFavourite = track.trackId in tracksFavouriteIds)
        }
        val state = SearchState.SearchResult(updatedTracks)
        renderState(state)
    }

    private fun executeSearch(searchText: String) {
        renderState(SearchState.Loading)
        viewModelScope.launch {
            searchTracksUseCase.searchTracks(searchText).collect { result: SearchResult ->
                processResult(result, searchText)
            }
        }
    }

    private fun processResult(result: SearchResult, searchText: String){
        when(result){
            is SearchResult.Success -> {
                if(result.tracks.isNullOrEmpty()){
                    renderState(SearchState.Empty(R.string.nothing_found))
                } else{
                    renderState(SearchState.SearchResult(result.tracks))
                }
                lastErrorSearch = null
            }
            is SearchResult.Error -> {
                renderState(SearchState.Error(R.string.connection_problems))
                lastErrorSearch = searchText
            }
        }
    }

    fun searchDebounce(text: String) {
        if (lastSearchText == text) {
            return
        }
        lastSearchText = text
        searchJob = viewModelScope.launch {
            searchJob?.cancel()
            delay(SEARCH_DELAY)
            if (lastSearchText.isNotEmpty()) {
                executeSearch(lastSearchText)
            }
        }
    }

    fun retryErrorSearch() {
        val lastError = lastErrorSearch
        if (lastError != null) {
            lastSearchText = lastError
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                if (lastSearchText.isNotEmpty()) {
                    executeSearch(lastSearchText)
                }
            }
        }
    }

    private fun renderState(state: SearchState) {
        _state.postValue(state)
    }

    private fun refreshHistory(){
        viewModelScope.launch {
            val history = historyUseCase.getHistory()
            _historyTracks.value = history
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}