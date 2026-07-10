package com.practicum.playlistmaker.features.player.ui.view_model

sealed interface BottomSheetUiState {
    object Show : BottomSheetUiState
    object Hide: BottomSheetUiState
}