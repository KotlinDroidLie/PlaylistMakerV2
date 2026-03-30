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
    private val appContext: Context,
    private val historyUseCase: IHistoryUseCase,
    private val searchTracksUseCase: ISearchTracksUseCase
) : ViewModel() {
    companion object{
        fun getViewModelFactory(
            historyUseCase: IHistoryUseCase,
            searchTracksUseCase: ISearchTracksUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appContext = (this[APPLICATION_KEY] as App)
                SearchViewModel(appContext, historyUseCase, searchTracksUseCase)
            }
        }
        private const val SEARCH_DELAY = 2000L
    }
    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state
    private val _historyTracks = MutableLiveData<List<TrackModel>>(historyUseCase.getHistory())
    val historyTracks: LiveData<List<TrackModel>> = _historyTracks
    private var lastSearchText: String = ""
    private var lastErrorSearch: String? = null
    private val handler = Handler(Looper.getMainLooper())

    fun saveToHistory(track: TrackModel){
        historyUseCase.saveToHistory(track)
        _historyTracks.postValue(historyUseCase.getHistory())
    }
    fun clearHistory(){
        historyUseCase.clearHistory()
        _historyTracks.postValue(historyUseCase.getHistory())
    }
    private fun executeSearch(searchText: String){
        renderState(SearchState.Loading)
        searchTracksUseCase.searchTracks(searchText, object : ISearchTracksUseCase.TracksConsumer{
            override fun consume(
                foundTracks: List<TrackModel>?,
                errorMessage: String?,
                typeError: ErrorType?
            ) {
                val tracksList = mutableListOf<TrackModel>()
                if (foundTracks != null){
                    tracksList.clear()
                    tracksList.addAll(foundTracks)
                }
                if(errorMessage != null){
                    renderState(
                        SearchState.Error(appContext.getString(R.string.connection_problems))
                    )
                    lastErrorSearch = searchText
                } else if (tracksList.isEmpty()){
                    renderState(SearchState.Empty(appContext.getString(R.string.nothing_found)))
                    lastErrorSearch = null
                } else {
                    renderState(SearchState.Content(tracksList))
                    lastErrorSearch = null
                }
            }

        })
    }

    private val searchRunnable = Runnable{
        if (lastSearchText.isNotEmpty()) {
            executeSearch(lastSearchText)
        }
    }

    fun searchDebounce(text: String){
        if (lastSearchText == text){
            return
        }
        lastSearchText = text
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable,SEARCH_DELAY)
    }
    fun retryErrorSearch(){
        val lastError = lastErrorSearch
        if(lastError != null){
            lastSearchText = lastError
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable,SEARCH_DELAY)
        }
    }
    private fun renderState(state: SearchState){
        _state.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

}