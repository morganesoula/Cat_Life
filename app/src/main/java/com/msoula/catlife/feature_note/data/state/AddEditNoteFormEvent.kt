package com.msoula.catlife.feature_note.data.state

import commsoulacatlifedatabase.CatEntity


interface AddEditNoteFormEvent {
    data class OnCatChanged(val cat: CatEntity) : AddEditNoteFormEvent
    data class OnTitleChanged(val title: String) : AddEditNoteFormEvent
    data class OnDescriptionChanged(val description: String) : AddEditNoteFormEvent
    data class OnNoteUpdatedEvent(val noteId: Int?) : AddEditNoteFormEvent
    object Submit : AddEditNoteFormEvent
}