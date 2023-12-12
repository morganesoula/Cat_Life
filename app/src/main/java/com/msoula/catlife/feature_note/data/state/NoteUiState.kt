package com.msoula.catlife.feature_note.data.state

data class NoteUiState(
    val openDeleteAlert: Boolean = false,
    val itemId: Int = -1
)
