package com.msoula.catlife.feature_main.data.state

import commsoulacatlifedatabase.CatEntity


sealed interface CatsFeedUiState {
    data object Loading : CatsFeedUiState
    data object Empty : CatsFeedUiState
    data class Success(
        val catsFeed: List<CatEntity>
    ) : CatsFeedUiState
}