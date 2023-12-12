package com.msoula.catlife.core.presentation.navigation

data class CatDetailScreenNavArgs(
    val catDetailId: Int
)

data class AddEditCatFormScreenNavArgs(
    val catUpdateId: Int? = -1
)

data class NoteDetailScreenNavArgs(
    val noteDetailId: Int
)

data class AddEditNoteFormScreenNavArgs(
    var noteUpdateId: Int? = -1
)

data class EventDetailScreenNavArgs(
    val eventDetailId: Int
)

data class AddEditEventFormScreenNavArgs(
    val eventUpdateId: Int? = -1,
    val currentDateSelected: Long? = -1
)

data class CalendarScreenNavArgs(
    val selectedStartDate: Long? = -1L
)