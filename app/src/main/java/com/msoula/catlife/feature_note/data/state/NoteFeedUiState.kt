package com.msoula.catlife.feature_note.data.state

import commsoulacatlifedatabase.CatEntity
import commsoulacatlifedatabase.Note

interface NoteFeedUiState {
    object Loading : NoteFeedUiState
    object Empty : NoteFeedUiState
    data class Success(val notes: List<Note>) : NoteFeedUiState
}

interface CatForNoteUiState {
    object Loading : CatForNoteUiState
    data class Success(val catForNote: List<CatEntity>) : CatForNoteUiState
}