package com.msoula.catlife.feature_note.data.state

import com.msoula.catlife.R
import commsoulacatlifedatabase.Note

data class AddEditNoteFormState(
    val currentNoteId: Int? = null,
    val currentCatId: Int = -1,
    val currentCatIdErrorMessage: Int? = null,
    val currentCatProfilePath: String = "",
    val currentCatName: String = "",
    val currentTitle: String = "",
    val currentDate: Long = 0L,
    val currentTitleErrorMessage: Int? = null,
    val currentDescription: String = "",
    val currentDescriptionErrorMessage: Int? = null,
    val enableSubmit: Boolean = false,
    val addEditNoteComposableTitle: Int = R.string.add_note_title,
    val submitNoteText: Int = R.string.submit_note_title,
    val lastInsertedNoteId: Int? = null
)

fun AddEditNoteFormState.mapToNote(): Note {
    return Note(
        id = this.currentNoteId?.toLong() ?: -1L,
        catId = this.currentCatId.toLong(),
        catName = this.currentCatName,
        catProfilePath = this.currentCatProfilePath,
        content = this.currentDescription,
        date = this.currentDate,
        title = this.currentTitle
    )
}
