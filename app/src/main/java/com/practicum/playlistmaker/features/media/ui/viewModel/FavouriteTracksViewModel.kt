package com.practicum.playlistmaker.features.media.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.features.media.domain.api.IFavouriteInteractor
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import kotlinx.coroutines.launch

class FavouriteTracksViewModel(
    private val favouriteInteractor: IFavouriteInteractor
): ViewModel() {
    private val _state = MutableLiveData<FavouriteTracksState>(FavouriteTracksState.Empty)
    val state: LiveData<FavouriteTracksState> = _state
    init {
        viewModelScope.launch {
            favouriteInteractor.getTracks().collect{ tracks ->
                processResult(tracks)
            }
        }
    }
    private fun processResult(tracks: List<TrackModel>){
        if(tracks.isEmpty()){
            renderState(FavouriteTracksState.Empty)
        }else{
            renderState(FavouriteTracksState.Content(tracks))
        }
    }

    private fun renderState(state: FavouriteTracksState){
        _state.postValue(state)
    }
}