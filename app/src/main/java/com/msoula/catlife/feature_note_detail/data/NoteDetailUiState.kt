package com.msoula.catlife.feature_note_detail.data

data class NoteDetailUiState(
    val noteId: Int? = null,
    val catId: Int = 0,
    val catProfilePath: String? = null,
    val catName: String? = null,
    val noteTitle: String = "",
    val noteDescription: String = "",
    val noteDeleted: Boolean = false,
    val openDeleteAlert: Boolean = false
)