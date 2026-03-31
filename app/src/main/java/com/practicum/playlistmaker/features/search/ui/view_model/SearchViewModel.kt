package com.practicum.playlistmaker.features.search.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.features.search.data.dto.ErrorType
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.domain.api.usecase.IHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase

class SearchViewModel(
    private val historyUseCase: IHistoryUseCase,
    private val searchTracksUseCase: ISearchTracksUseCase
) : ViewModel() {
    private val _state = MutableLiveData<SearchState>(
        SearchState.History(historyUseCase.getHistory())
    )
    val state: LiveData<SearchState> = _state
    private var lastSearchText: String = ""
    private var lastErrorSearch: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        if (lastSearchText.isNotEmpty()) {
            executeSearch(lastSearchText)
        }
    }

    fun saveToHistory(track: TrackModel) {
        historyUseCase.saveToHistory(track)
        _state.postValue(
            SearchState.History(historyUseCase.getHistory())
        )
    }

    fun clearHistory() {
        historyUseCase.clearHistory()
        _state.postValue(
            SearchState.History(historyUseCase.getHistory())
        )
    }

    private fun executeSearch(searchText: String) {
        renderState(SearchState.Loading)
        searchTracksUseCase.searchTracks(searchText, object : ISearchTracksUseCase.TracksConsumer {
            override fun consume(
                foundTracks: List<TrackModel>?,
                errorMessage: Int?,
                typeError: ErrorType?,
                extraMessage: String?
            ) {
                when {
                    errorMessage != null -> {
                        renderState(
                            SearchState.Error(R.string.connection_problems)
                        )
                        lastErrorSearch = searchText
                    }

                    foundTracks.isNullOrEmpty() -> {
                        renderState(SearchState.Empty(R.string.nothing_found))
                        lastErrorSearch = null
                    }

                    else -> {
                        renderState(SearchState.Content(foundTracks))
                        lastErrorSearch = null
                    }
                }
            }

        })
    }

    fun searchDebounce(text: String) {
        if (lastSearchText == text) {
            return
        }
        lastSearchText = text
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DELAY)
    }

    fun retryErrorSearch() {
        val lastError = lastErrorSearch
        if (lastError != null) {
            lastSearchText = lastError
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DELAY)
        }
    }

    private fun renderState(state: SearchState) {
        _state.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        fun getViewModelFactory(
            historyUseCase: IHistoryUseCase,
            searchTracksUseCase: ISearchTracksUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(historyUseCase, searchTracksUseCase)
            }
        }

        private const val SEARCH_DELAY = 2000L
    }
}